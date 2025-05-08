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
        if (request.getLoanDateTime() == null || request.getReturnDueDateTime() == null) {
            throw new IllegalArgumentException("Dates must not be null.");
        }
        if (request.getLoanDateTime().isAfter(request.getReturnDueDateTime())) {
            throw new IllegalArgumentException("Return date must be after the loan date.");
        }

        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));

        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new IllegalStateException("The equipment is not available for loan.");
        }

        equipment.setStatus(EquipmentStatus.LOANED);
        equipmentRepository.save(equipment);

        Loan loan = new Loan();
        loan.setUserId(request.getUserId());
        loan.setEquipmentId(equipment.getId());
        loan.setLoanDateTime(request.getLoanDateTime());
        loan.setReturnDueDateTime(request.getReturnDueDateTime());
        loan.setLoanDurationHours(request.getLoanDurationHours());
        loan.setInitialEquipmentStatus(EquipmentStatus.AVAILABLE);

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
        Loan loan = loanRepository.findById(details.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.isReturned()) {
            throw new IllegalStateException("The loan has already been returned.");
        }

        Equipment equipment = equipmentRepository.findById(loan.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        loan.setReturned(true);
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
