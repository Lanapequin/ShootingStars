package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.repository.EquipmentRepository;
import eci.cvds.tdd.module.sportLoan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ServiceLoan implements LoanService {

    @Autowired
    private  LoanRepository loanRepository;
    @Autowired
    private  EquipmentRepository equipmentRepository;

    @Override
    public Loan createLoan(LoanRequest request) {
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new RuntimeException("Equipment not available");
        }

        if (request.getLoanDateTime().isAfter(request.getReturnDueDateTime())){
            throw new RuntimeException("Invalid Date");
        }

        equipment.setStatus(EquipmentStatus.LOANED);
        equipmentRepository.save(equipment);

        Loan loan = new Loan();
        loan.setUserId(request.getUserId());
        loan.setEquipmentId(equipment.getId());
        loan.setLoanDateTime(request.getLoanDateTime());
        loan.setReturnDueDateTime(request.getReturnDueDateTime());
        loan.setLoanDurationHours(request.getLoanDurationHours());
        loan.setInitialEquipmentStatus(equipment.getStatus());

        return loanRepository.save(loan);
    }


    @Override
    public void cancelLoan(String loanId) {
        Optional<Loan> loan = loanRepository.findById(loanId);

        Optional<Equipment> equipment = equipmentRepository.findById(loan.get().getEquipmentId());

        equipment.get().setStatus(EquipmentStatus.AVAILABLE);
        equipmentRepository.save(equipment.get());


        loanRepository.deleteById(loanId);
    }

    @Override
    public Loan returnLoan(ReturnDetails details) {
        Loan loan = loanRepository.findById(details.getLoanId()).orElseThrow(() -> new RuntimeException("Loan not found"));
        Equipment equipment = equipmentRepository.findById(loan.getEquipmentId()).orElseThrow(() -> new RuntimeException("Equipment not found"));

        Map<String, EquipmentStatus> returnStatus = new HashMap<>();
        returnStatus.put(equipment.getId(), details.getReturnStatus());
        loan.setReturnEquipmentStatus(details.getReturnStatus());
        loan.setObservations(details.getObservations());
        loan.setConfirmedReturnBy(details.getConfirmedReturnBy());
        loan.setReturnDateTime(LocalDateTime.now());

        equipment.setStatus(details.getReturnStatus());
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    @Override
    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId).orElse(null);
    }

    @Override
    public List<Loan> listLoansByUser(String userId) {
        return loanRepository.findByUserId(userId);
    }

    @Override
    public List<Loan> listLoansByDateRange(Date from, Date to) {
        return loanRepository.findByLoanDateTimeBetween(from, to);
    }

    @Override
    public void sendReturnReminder(String loanId) {
        // Lógica para enviar recordatorios (por correo, notificación, etc.)
    }
}
