//package eci.cvds.tdd.module.sportLoan.repository;
//
//import eci.cvds.tdd.module.sportLoan.model.Loan;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class LoanRepositoryTest {
//
//    @Test
//    void testFindAllByReturnedFalse() {
//        // Crear dos préstamos simulados: uno devuelto y otro no
//        Loan loanReturned = mock(Loan.class);
//        when(loanReturned.isReturned()).thenReturn(true);
//
//        Loan loanNotReturned = mock(Loan.class);
//        when(loanNotReturned.isReturned()).thenReturn(false);
//
//        // Crear un mock del repositorio
//        LoanRepository loanRepository = mock(LoanRepository.class, Mockito.CALLS_REAL_METHODS);
//        when(loanRepository.findAll()).thenReturn(Arrays.asList(loanReturned, loanNotReturned));
//
//        // Ejecutar el método
//        List<Loan> result = loanRepository.findAllByReturnedFalse();
//
//        // Verificaciones
//        assertEquals(1, result.size());
//        assertSame(loanNotReturned, result.get(0)); // Solo el préstamo no devuelto debe estar
//    }
//}
