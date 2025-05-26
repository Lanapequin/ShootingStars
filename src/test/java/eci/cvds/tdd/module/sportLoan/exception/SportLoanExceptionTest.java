package eci.cvds.tdd.module.sportLoan.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SportLoanExceptionTest {

    @Test
    void testBaseException() {
        SportLoanException exception = new SportLoanException("Base exception");
        assertEquals("Base exception", exception.getMessage());
    }

    @Test
    void testEquipmentNotAvailableException() {
        SportLoanException ex = new SportLoanException.EquipmentNotAvailableException("Not available");
        assertTrue(ex instanceof SportLoanException.EquipmentNotAvailableException);
        assertEquals("Not available", ex.getMessage());
    }

    @Test
    void testEquipmentNotFoundException() {
        SportLoanException ex = new SportLoanException.EquipmentNotFoundException("Not found");
        assertTrue(ex instanceof SportLoanException.EquipmentNotFoundException);
        assertEquals("Not found", ex.getMessage());
    }

    @Test
    void testLoanNotFoundException() {
        SportLoanException ex = new SportLoanException.LoanNotFoundException("Loan not found");
        assertTrue(ex instanceof SportLoanException.LoanNotFoundException);
        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    void testUserNotFoundException() {
        SportLoanException ex = new SportLoanException.UserNotFoundException("User not found");
        assertTrue(ex instanceof SportLoanException.UserNotFoundException);
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testLoanAlreadyReturnedException() {
        SportLoanException ex = new SportLoanException.LoanAlreadyReturnedException("Already returned");
        assertTrue(ex instanceof SportLoanException.LoanAlreadyReturnedException);
        assertEquals("Already returned", ex.getMessage());
    }
}
