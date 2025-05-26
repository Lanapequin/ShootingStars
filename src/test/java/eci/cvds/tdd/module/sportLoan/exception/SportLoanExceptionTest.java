package eci.cvds.tdd.module.sportLoan.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SportLoanExceptionTest {

    @Test
    public void testEquipmentNotAvailableException() {
        String message = "Equipo no disponible";
        SportLoanException ex = new SportLoanException.EquipmentNotAvailableException(message);

        assertTrue(ex instanceof SportLoanException);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testEquipmentNotFoundException() {
        String message = "Equipo no encontrado";
        SportLoanException ex = new SportLoanException.EquipmentNotFoundException(message);

        assertTrue(ex instanceof SportLoanException);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testLoanNotFoundException() {
        String message = "Préstamo no encontrado";
        SportLoanException ex = new SportLoanException.LoanNotFoundException(message);

        assertTrue(ex instanceof SportLoanException);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testUserNotFoundException() {
        String message = "Usuario no encontrado";
        SportLoanException ex = new SportLoanException.UserNotFoundException(message);

        assertTrue(ex instanceof SportLoanException);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testLoanAlreadyReturnedException() {
        String message = "El préstamo ya fue devuelto";
        SportLoanException ex = new SportLoanException.LoanAlreadyReturnedException(message);

        assertTrue(ex instanceof SportLoanException);
        assertEquals(message, ex.getMessage());
    }
}
