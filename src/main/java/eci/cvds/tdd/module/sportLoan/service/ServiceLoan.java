package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.exception.SportLoanException;
import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.model.User;
import eci.cvds.tdd.module.sportLoan.repository.EquipmentRepository;
import eci.cvds.tdd.module.sportLoan.repository.LoanRepository;
import eci.cvds.tdd.module.sportLoan.repository.UserRepository;
import eci.cvds.tdd.module.sportLoan.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

import static eci.cvds.tdd.module.sportLoan.util.DateValidator.esHorarioPermitido;

/**
 * Servicio para la gestión de préstamos de equipos deportivos.
 * Proporciona métodos para crear, cancelar, devolver y consultar préstamos.
 */
@Service
public class ServiceLoan implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Crea un nuevo préstamo de equipo.
     *
     * @param request Detalles del préstamo.
     * @return El préstamo creado.
     * @throws IllegalArgumentException Si las fechas son inválidas.
     * @throws SportLoanException Si el equipo no existe o no está disponible.
     */
    @Override
    public Loan createLoan(LoanRequest request) {
        // Validación de fechas nulas o en orden incorrecto
        if (request.getLoanDateTime() == null || request.getReturnDueDateTime() == null) {
            throw new IllegalArgumentException("Dates must not be null.");
        }
        if (request.getLoanDateTime().isAfter(request.getReturnDueDateTime())) {
            throw new IllegalArgumentException("Return date must be after the loan date.");
        }

        if (!esHorarioPermitido(request.getLoanDateTime()) || !esHorarioPermitido(request.getReturnDueDateTime())) {
            throw new IllegalArgumentException("Loan and return times must be within allowed hours: Mon–Fri 7am–4pm, Sat 8am–12pm.");
        }

        if(request.getUserId().isEmpty()||request.getUserId()==null){
            throw new IllegalArgumentException("User ID must not be null.");
        }

        // Validar que el equipo exista
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new SportLoanException.EquipmentNotFoundException(
                        "Equipment with ID " + request.getEquipmentId() + " not found."));

        // Validar que el equipo esté disponible y en buen estado
        if (!equipment.isAvailable()) {
            throw new SportLoanException.EquipmentNotAvailableException("The equipment is not available for loan.");
        }

        if (equipment.getStatus().equals(EquipmentStatus.DAMAGED) ||
                equipment.getStatus().equals(EquipmentStatus.MAINTENANCE)) {
            throw new SportLoanException.EquipmentNotAvailableException(
                    "The equipment is currently " + equipment.getStatus().name().toLowerCase() + ".");
        }

        // Actualizar estado del equipo y guardar el préstamo
        equipment.setAvailable(false);
        equipmentRepository.save(equipment);

        Loan loan = new Loan();
        loan.setUserId(request.getUserId());
        loan.setEquipmentId(equipment.getId());
        loan.setLoanDateTime(request.getLoanDateTime());
        loan.setReturnDueDateTime(request.getReturnDueDateTime());
        loan.setLoanDurationHours(request.getLoanDurationHours());
        loan.setInitialEquipmentStatus(EquipmentStatus.GOODSTATUS);

        return loanRepository.save(loan);
    }

    @Override
    public void addLoanToUser(Loan loan){
        if(!userRepository.existsById(loan.getUserId())){
            User user=new User();
            user.setId(loan.getUserId());
            List<Loan> loans=new ArrayList<>();
            loans.add(loan);
            user.setLoans(loans);
            userRepository.save(user);
        }
        else{
            User user=userRepository.findUserById(loan.getUserId());
            user.getLoans().add(loan);
            userRepository.save(user);
        }
    }



    /**
     * Cancela un préstamo existente y actualiza el estado del equipo.
     *
     * @param loanId ID del préstamo a cancelar.
     */
    @Override
    public void cancelLoan(String loanId) {
        Optional<Loan> loan = loanRepository.findById(loanId);
        Optional<Equipment> equipment = equipmentRepository.findById(loan.get().getEquipmentId());

        equipment.get().setStatus(EquipmentStatus.GOODSTATUS);
        equipmentRepository.save(equipment.get());

        loanRepository.deleteById(loanId);
    }

    /**
     * Marca un préstamo como devuelto.
     *
     * @param details Detalles del retorno del equipo.
     * @return Préstamo actualizado.
     * @throws SportLoanException Si el préstamo no existe o ya fue devuelto.
     */
    @Override
    public Loan returnLoan(ReturnDetails details) {
        Loan loan = loanRepository.findById(details.getLoanId())
                .orElseThrow(() -> new SportLoanException.LoanNotFoundException(
                        "Loan with ID " + details.getLoanId() + " not found."));

        if (loan.isReturned()) {
            throw new SportLoanException.LoanAlreadyReturnedException("The loan has already been returned.");
        }

        Equipment equipment = equipmentRepository.findById(loan.getEquipmentId())
                .orElseThrow(() -> new SportLoanException.EquipmentNotFoundException(
                        "Equipment with ID " + loan.getEquipmentId() + " not found."));

        User user = userRepository.findUserById(loan.getUserId());
        if (user == null) {
            throw new SportLoanException.UserNotFoundException("User with ID " + loan.getUserId() + " not found.");
        }
        if (user.getLoans().contains(loan)) {
            user.getLoans().remove(loan);
            userRepository.save(user);
        }

        loan.setReturned(true);
        loan.setReturnEquipmentStatus(details.getReturnStatus());
        loan.setObservations(details.getObservations());
        loan.setConfirmedReturnBy(details.getConfirmedReturnBy());
        loan.setReturnDateTime(LocalDateTime.now());

        equipment.setStatus(details.getReturnStatus());
        equipment.setAvailable(true);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    /**
     * Consulta un préstamo por su ID.
     *
     * @param loanId ID del préstamo.
     * @return Préstamo encontrado o null si no existe.
     */
    @Override
    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId).orElse(null);
    }

    /**
     * Lista los préstamos de un usuario.
     *
     * @param userId ID del usuario.
     * @return Lista de préstamos del usuario.
     */
    @Override
    public List<Loan> listLoansByUser(String userId) {
        return loanRepository.findByUserId(userId);
    }

    /**
     * Lista préstamos dentro de un rango de fechas.
     *
     * @param from Fecha de inicio.
     * @param to Fecha de fin.
     * @return Lista de préstamos en el rango especificado.
     */
    @Override
    public List<Loan> listLoansByDateRange(Date from, Date to) {
        return loanRepository.findByLoanDateTimeBetween(from, to);
    }

    /**
     * Envía una notificación si el préstamo está próximo a vencerse.
     *
     * @param loanId ID del préstamo.
     */
    @Override
    public void sendReturnReminder(String loanId) {
        Loan loan = loanRepository.findLoanById(loanId);
        if (loan == null) {
            throw new SportLoanException.LoanNotFoundException("Loan with ID " + loanId + " not found.");
        }
        if (loan.isReturned()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime due = loan.getReturnDueDateTime();

        if (!now.isAfter(due) && now.plusMinutes(5).isAfter(due)) {
            System.out.println("Notificación al usuario " + loan.getUserId() +
                    ": Debe devolver el equipo " + loan.getEquipmentId() + " en " + due + ".");
        }
    }

    /**
     * Tarea programada que revisa periódicamente los préstamos activos y envía recordatorios.
     * Se ejecuta cada 20 minutos.
     */
    @Scheduled(fixedRate = 1200000)
    public void checkLoansAndSendReminders() {
        List<Loan> activeLoans = loanRepository.findAllByReturnedFalse();
        for (Loan loan : activeLoans) {
            sendReturnReminder(loan.getId());
            sendReturnOutReminder(loan.getId());
        }
    }

    /**
     * Envía una notificación si el préstamo ya se ha vencido.
     *
     * @param loanId ID del préstamo.
     */
    @Override
    public void sendReturnOutReminder(String loanId) {
        Loan loan = loanRepository.findLoanById(loanId);
        if (loan == null) {
            throw new SportLoanException.LoanNotFoundException("Loan with ID " + loanId + " not found.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime due = loan.getReturnDueDateTime();

        if (now.isAfter(due)) {
            System.out.println("Notificación al usuario " + loan.getUserId() +
                    ": Debe devolver el equipo.");
        }
    } 

    @Override
    public List<String> getNotificationsForUser(String userId) {
        List<String> result = new ArrayList<>();
        List<Loan> activeLoans = loanRepository.findAllByUserIdAndReturnedFalse(userId);
        LocalDateTime now = LocalDateTime.now();
        
        for (Loan loan : activeLoans) {
            LocalDateTime due = loan.getReturnDueDateTime();
            if (!now.isAfter(due) && now.plusMinutes(5).isAfter(due)) {
                result.add("Debe devolver el equipo " + loan.getEquipmentId() + " en " + due + ".");
            } else if (now.isAfter(due)) {
                result.add("Debe devolver el equipo " + loan.getEquipmentId() + ". Ya está vencido.");
            }
        }
        return result;
    }


}
