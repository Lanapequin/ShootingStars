package eci.cvds.tdd.module.sportLoan.controller;

import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanControllerTest {

    @InjectMocks
    private LoanController loanController;

    @Mock
    private LoanService loanService;

    private final String dummyToken = "Bearer sometoken";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLoan_Success_ReturnsCreatedLoan() {
        LoanRequest request = new LoanRequest();
        Loan loan = new Loan();
        when(loanService.createLoan(request)).thenReturn(loan);

        ResponseEntity<?> response = loanController.createLoan(request, dummyToken);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(loan, response.getBody());
        verify(loanService).createLoan(request);
        verify(loanService).addLoanToUser(loan);
        verify(loanService).sendReturnReminder(loan.getId());
    }

    @Test
    void createLoan_Exception_ReturnsBadRequest() {
        LoanRequest request = new LoanRequest();
        when(loanService.createLoan(request)).thenThrow(new RuntimeException("Error creating loan"));

        ResponseEntity<?> response = loanController.createLoan(request, dummyToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error creating loan", response.getBody());
    }

    @Test
    void returnLoan_ReturnsUpdatedLoan() {
        ReturnDetails details = new ReturnDetails();
        Loan loan = new Loan();
        when(loanService.returnLoan(details)).thenReturn(loan);

        ResponseEntity<Loan> response = loanController.returnLoan(details, dummyToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loan, response.getBody());
    }

    @Test
    void getLoanById_Found_ReturnsLoan() {
        String loanId = "123";
        Loan loan = new Loan();
        when(loanService.getLoanById(loanId)).thenReturn(loan);

        ResponseEntity<Loan> response = loanController.getLoanById(loanId, dummyToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loan, response.getBody());
    }

    @Test
    void getLoanById_NotFound_Returns404() {
        String loanId = "123";
        when(loanService.getLoanById(loanId)).thenReturn(null);

        ResponseEntity<Loan> response = loanController.getLoanById(loanId, dummyToken);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void listLoansByUser_ReturnsList() {
        String userId = "user1";
        List<Loan> loans = List.of(new Loan(), new Loan());
        when(loanService.listLoansByUser(userId)).thenReturn(loans);

        ResponseEntity<List<Loan>> response = loanController.listLoansByUser(userId, dummyToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loans, response.getBody());
    }

    @Test
    void listLoansByDateRange_ReturnsList() {
        Date from = new Date();
        Date to = new Date();
        List<Loan> loans = List.of(new Loan());
        when(loanService.listLoansByDateRange(from, to)).thenReturn(loans);

        ResponseEntity<List<Loan>> response = loanController.listLoansByDateRange(from, to, dummyToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loans, response.getBody());
    }

    @Test
    void listLoansByDateRange_ActiveLoans_ReturnsList() {
        List<Loan> loans = List.of(new Loan());
        when(loanService.getActiveLoans()).thenReturn(loans);

        ResponseEntity<List<Loan>> response = loanController.listLoansByDateRange(dummyToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loans, response.getBody());
    }

    @Test
    void getUserNotifications_ReturnsList() {
        String userId = "user1";
        List<String> notifications = List.of("Notification 1", "Notification 2");
        when(loanService.getNotificationsForUser(userId)).thenReturn(notifications);

        ResponseEntity<List<String>> response = loanController.getUserNotifications(userId, dummyToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notifications, response.getBody());
    }
}
