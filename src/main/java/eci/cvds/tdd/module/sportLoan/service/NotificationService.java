package eci.cvds.tdd.module.sportLoan.service;

public interface NotificationService {
    void sendLoanConfirmation(String userId, String loanId);
    void sendReturnDeadlineReminder(String userId, String loanId);
    void sendLateReturnAlert(String userId, String loanId);
}
