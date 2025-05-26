//package eci.cvds.tdd.module.sportLoan.exception;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SportLoanExceptionTest {
//
//    @Test
//    void testBaseExceptionMessage() {
//        String message = "General error";
//        SportLoanException exception = new SportLoanException(message);
//        assertEquals(message, exception.getMessage());
//    }
//
//    @Test
//    void testEquipmentNotAvailableException() {
//        String message = "Equipment not available";
//        SportLoanException.EquipmentNotAvailableException exception =
//                new SportLoanException.EquipmentNotAvailableException(message);
//        assertEquals(message, exception.getMessage());
//    }
//
//    @Test
//    void testEquipmentNotFoundException() {
//        String message = "Equipment not found";
//        SportLoanException.EquipmentNotFoundException exception =
//                new SportLoanException.EquipmentNotFoundException(message);
//        assertEquals(message, exception.getMessage());
//    }
//
//    @Test
//    void testLoanNotFoundException() {
//        String message = "Loan not found";
//        SportLoanException.LoanNotFoundException exception =
//                new SportLoanException.LoanNotFoundException(message);
//        assertEquals(message, exception.getMessage());
//    }
//
//    @Test
//    void testUserNotFoundException() {
//        String message = "User not found";
//        SportLoanException.UserNotFoundException exception =
//                new SportLoanException.UserNotFoundException(message);
//        assertEquals(message, exception.getMessage());
//    }
//
//    @Test
//    void testLoanAlreadyReturnedException() {
//        String message = "Loan already returned";
//        SportLoanException.LoanAlreadyReturnedException exception =
//                new SportLoanException.LoanAlreadyReturnedException(message);
//        assertEquals(message, exception.getMessage());
//    }
//}
