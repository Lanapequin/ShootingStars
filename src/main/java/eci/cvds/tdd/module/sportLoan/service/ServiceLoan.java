package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Loan;

import java.util.Date;
import java.util.List;

public class ServiceLoan implements LoanService{
    @Override
    public Loan createLoan(LoanRequest request) {
        return null;
    }

    @Override
    public void cancelLoan(String loanId) {

    }

    @Override
    public Loan returnLoan(ReturnDetails details) {
        return null;
    }

    @Override
    public Loan getLoanById(String loanId) {
        return null;
    }

    @Override
    public List<Loan> listLoansByUser(String userId) {
        return List.of();
    }

    @Override
    public List<Loan> listLoansByDateRange(Date from, Date to) {
        return List.of();
    }

    @Override
    public void sendReturnReminder(String loanId) {

    }
}
