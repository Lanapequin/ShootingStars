package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Loan;

import java.util.Date;
import java.util.List;

public interface LoanService {
    Loan createLoan(LoanRequest request);
    void cancelLoan(String loanId);
    Loan returnLoan(ReturnDetails details);
    Loan getLoanById(String loanId);
    List<Loan> listLoansByUser(String userId);
    List<Loan> listLoansByDateRange(Date from, Date to);
    void sendReturnReminder(String loanId);
}
