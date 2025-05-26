package eci.cvds.tdd.module.sportLoan.controller;


import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLoanSuccess() {
        LoanRequest request = new LoanRequest();
        Loan loan = new Loan();
        when(loanService.createLoan(request)).thenReturn(loan);

        ResponseEntity<?> response = loanController.createLoan(request, "Bearer xyz");

        verify(loanService).addLoanToUser(loan);
        verify(loanService).sendReturnReminder(loan.getId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(loan, response.getBody());
    }

    @Test
    void testCreateLoanFailure() {
        LoanRequest request = new LoanRequest();
        when(loanService.createLoan(request)).thenThrow(new RuntimeException("error"));

        ResponseEntity<?> response = loanController.createLoan(request, "Bearer xyz");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody());
    }

    @Test
    void testReturnLoan() {
        ReturnDetails details = new ReturnDetails();
        Loan loan = new Loan();
        when(loanService.returnLoan(details)).thenReturn(loan);

        ResponseEntity<Loan> response = loanController.returnLoan(details, "Bearer xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loan, response.getBody());
    }

    @Test
    void testGetLoanByIdFound() {
        Loan loan = new Loan();
        when(loanService.getLoanById("123")).thenReturn(loan);

        ResponseEntity<Loan> response = loanController.getLoanById("123", "Bearer xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loan, response.getBody());
    }

    @Test
    void testGetLoanByIdNotFound() {
        when(loanService.getLoanById("123")).thenReturn(null);

        ResponseEntity<Loan> response = loanController.getLoanById("123", "Bearer xyz");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testListLoansByUser() {
        List<Loan> loans = Arrays.asList(new Loan(), new Loan());
        when(loanService.listLoansByUser("456")).thenReturn(loans);

        ResponseEntity<List<Loan>> response = loanController.listLoansByUser("456", "Bearer xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loans, response.getBody());
    }

    @Test
    void testListLoansByDateRange() {
        Date from = new Date();
        Date to = new Date();
        List<Loan> loans = Arrays.asList(new Loan());
        when(loanService.listLoansByDateRange(from, to)).thenReturn(loans);

        ResponseEntity<List<Loan>> response = loanController.listLoansByDateRange(from, to, "Bearer xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loans, response.getBody());
    }

    @Test
    void testListActiveLoans() {
        List<Loan> loans = Arrays.asList(new Loan());
        when(loanService.getActiveLoans()).thenReturn(loans);

        ResponseEntity<List<Loan>> response = loanController.listActiveLoans("Bearer xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loans, response.getBody());
    }

    @Test
    void testAllLoans() {
        List<Loan> loans = Arrays.asList(new Loan());
        when(loanService.getLoans()).thenReturn(loans);

        ResponseEntity<List<Loan>> response = loanController.allLoans("Bearer xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loans, response.getBody());
    }

    @Test
    void testGetUserNotifications() {
        List<String> notifications = Arrays.asList("Notificación 1", "Notificación 2");
        when(loanService.getNotificationsForUser("789")).thenReturn(notifications);

        ResponseEntity<List<String>> response = loanController.getUserNotifications("789", "Bearer xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notifications, response.getBody());
    }
}

