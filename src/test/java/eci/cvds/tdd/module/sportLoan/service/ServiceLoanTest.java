package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.exception.SportLoanException;
import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.model.User;
import eci.cvds.tdd.module.sportLoan.repository.EquipmentRepository;
import eci.cvds.tdd.module.sportLoan.repository.LoanRepository;
import eci.cvds.tdd.module.sportLoan.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceLoanTest {

    @InjectMocks
    private ServiceLoan serviceLoan;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private UserRepository userRepository;

    private Equipment equipment;
    private LoanRequest loanRequest;
    private Loan loan;
    private User user;

    @BeforeEach
    public void setup() {
        equipment = new Equipment();
        equipment.setId("eq1");
        equipment.setAvailable(true);
        equipment.setStatus(EquipmentStatus.GOODSTATUS);

        loanRequest = new LoanRequest();
        loanRequest.setEquipmentId("eq1");
        loanRequest.setUserId("123456789L");
        loanRequest.setLoanDateTime(LocalDateTime.now().minusHours(1));
        loanRequest.setReturnDueDateTime(LocalDateTime.now().plusHours(2));
        loanRequest.setLoanDurationHours(3);

        loan = new Loan();
        loan.setId("loan1");
        loan.setUserId("123456789L");
        loan.setEquipmentId("eq1");
        loan.setLoanDateTime(loanRequest.getLoanDateTime());
        loan.setReturnDueDateTime(loanRequest.getReturnDueDateTime());
        loan.setReturned(false);
        loan.setInitialEquipmentStatus(EquipmentStatus.GOODSTATUS);

        user = new User();
        user.setId("123456789L");
        user.setLoans(new ArrayList<>());
    }

    // --- createLoan tests ---
    @Test
    public void createLoan_Success() {
        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
        when(equipmentRepository.save(any())).thenReturn(equipment);
        when(loanRepository.save(any())).thenAnswer(i -> {
            Loan l = i.getArgument(0);
            l.setId("loan1");
            return l;
        });

        Loan created = serviceLoan.createLoan(loanRequest);

        assertNotNull(created);
        assertEquals("loan1", created.getId());
        assertFalse(equipment.isAvailable());
        verify(equipmentRepository).save(equipment);
        verify(loanRepository).save(any());
    }

    @Test
    public void createLoan_ThrowsDatesNull() {
        loanRequest.setLoanDateTime(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> serviceLoan.createLoan(loanRequest));
        assertEquals("Dates must not be null.", ex.getMessage());
    }

    @Test
    public void createLoan_ThrowsReturnBeforeLoan() {
        loanRequest.setReturnDueDateTime(loanRequest.getLoanDateTime().minusHours(1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> serviceLoan.createLoan(loanRequest));
        assertEquals("Return date must be after the loan date.", ex.getMessage());
    }

    @Test
    public void createLoan_ThrowsEquipmentNotFound() {
        when(equipmentRepository.findById("eq1")).thenReturn(Optional.empty());

        SportLoanException.EquipmentNotFoundException ex = assertThrows(
                SportLoanException.EquipmentNotFoundException.class,
                () -> serviceLoan.createLoan(loanRequest));
        assertTrue(ex.getMessage().contains("Equipment with ID eq1 not found."));
    }

    @Test
    public void createLoan_ThrowsEquipmentNotAvailable() {
        equipment.setAvailable(false);
        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));

        SportLoanException.EquipmentNotAvailableException ex = assertThrows(
                SportLoanException.EquipmentNotAvailableException.class,
                () -> serviceLoan.createLoan(loanRequest));
        assertEquals("The equipment is not available for loan.", ex.getMessage());
    }

    @Test
    public void createLoan_ThrowsWhenStatusDamagedOrMaintenance() {
        equipment.setStatus(EquipmentStatus.DAMAGED);
        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
        SportLoanException.EquipmentNotAvailableException ex1 = assertThrows(
                SportLoanException.EquipmentNotAvailableException.class,
                () -> serviceLoan.createLoan(loanRequest));
        assertTrue(ex1.getMessage().contains("damaged"));

        equipment.setStatus(EquipmentStatus.MAINTENANCE);
        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
        SportLoanException.EquipmentNotAvailableException ex2 = assertThrows(
                SportLoanException.EquipmentNotAvailableException.class,
                () -> serviceLoan.createLoan(loanRequest));
        assertTrue(ex2.getMessage().contains("maintenance"));
    }

    // --- addLoanToUser tests ---
    @Test
    public void addLoanToUser_NewUser() {
        when(userRepository.existsById(123456789L)).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        serviceLoan.addLoanToUser(loan);

        verify(userRepository).save(any(User.class));
    }

//    @Test
//    public void addLoanToUser_ExistingUser() {
//        when(userRepository.existsById("123456789L")).thenReturn(true);
//        when(userRepository.findById("123456789L")).thenReturn(user);
//        when(userRepository.save(any())).thenReturn(user);
//
//        serviceLoan.addLoanToUser(loan);
//
//        assertTrue(user.getLoans().contains(loan));
//        verify(userRepository).save(user);
//    }

    // --- cancelLoan tests ---
    @Test
    public void cancelLoan_Success() {
        when(loanRepository.findById("loan1")).thenReturn(Optional.of(loan));
        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
        doNothing().when(loanRepository).deleteById("loan1");
        when(equipmentRepository.save(any())).thenReturn(equipment);

        serviceLoan.cancelLoan("loan1");

        assertEquals(EquipmentStatus.GOODSTATUS, equipment.getStatus());
        verify(equipmentRepository).save(equipment);
        verify(loanRepository).deleteById("loan1");
    }

    // --- returnLoan tests ---
//    @Test
//    public void returnLoan_Success() {
//        ReturnDetails details = new ReturnDetails();
//        details.setLoanId("loan1");
//        details.setReturnStatus(EquipmentStatus.GOODSTATUS);
//        details.setObservations("Everything fine");
//        details.setConfirmedReturnBy("admin");
//
//        user.getLoans().add(loan);
//
//        when(loanRepository.findById("loan1")).thenReturn(Optional.of(loan));
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
//        when(userRepository.findById("123456789L")).thenReturn(user);
//        when(userRepository.save(any())).thenReturn(user);
//        when(equipmentRepository.save(any())).thenReturn(equipment);
//        when(loanRepository.save(any())).thenAnswer(i -> i.getArgument(0));
//
//        Loan returnedLoan = serviceLoan.returnLoan(details);
//
//        assertTrue(returnedLoan.isReturned());
//        assertEquals(EquipmentStatus.GOODSTATUS, returnedLoan.getReturnEquipmentStatus());
//        assertEquals("Everything fine", returnedLoan.getObservations());
//        assertEquals("admin", returnedLoan.getConfirmedReturnBy());
//        assertTrue(equipment.isAvailable());
//        assertEquals(EquipmentStatus.GOODSTATUS, equipment.getStatus());
//        assertFalse(user.getLoans().contains(loan));
//    }

    @Test
    public void returnLoan_ThrowsLoanNotFound() {
        when(loanRepository.findById("loan1")).thenReturn(Optional.empty());

        ReturnDetails details = new ReturnDetails();
        details.setLoanId("loan1");

        SportLoanException.LoanNotFoundException ex = assertThrows(
                SportLoanException.LoanNotFoundException.class,
                () -> serviceLoan.returnLoan(details));
        assertTrue(ex.getMessage().contains("Loan with ID loan1 not found."));
    }

    @Test
    public void returnLoan_ThrowsLoanAlreadyReturned() {
        loan.setReturned(true);
        when(loanRepository.findById("loan1")).thenReturn(Optional.of(loan));

        ReturnDetails details = new ReturnDetails();
        details.setLoanId("loan1");

        SportLoanException.LoanAlreadyReturnedException ex = assertThrows(
                SportLoanException.LoanAlreadyReturnedException.class,
                () -> serviceLoan.returnLoan(details));
        assertEquals("The loan has already been returned.", ex.getMessage());
    }
    // --- getLoanById tests ---
    @Test
    public void getLoanById_ReturnsLoan() {
        when(loanRepository.findById("loan1")).thenReturn(Optional.of(loan));

        Loan found = serviceLoan.getLoanById("loan1");

        assertNotNull(found);
        assertEquals("loan1", found.getId());
    }

    @Test
    public void getLoanById_ReturnsNullWhenNotFound() {
        when(loanRepository.findById("loan1")).thenReturn(Optional.empty());

        Loan found = serviceLoan.getLoanById("loan1");

        assertNull(found);
    }

    // --- listLoansByUser tests ---
    @Test
    public void listLoansByUser_ReturnsLoans() {
        List<Loan> loans = List.of(loan);
        when(loanRepository.findByUserId("user1")).thenReturn(loans);

        List<Loan> result = serviceLoan.listLoansByUser("user1");

        assertEquals(1, result.size());
        assertEquals("loan1", result.get(0).getId());
    }

    // --- listLoansByDateRange tests ---
    @Test
    public void listLoansByDateRange_ReturnsLoans() {
        Date from = new Date(System.currentTimeMillis() - 1000000);
        Date to = new Date(System.currentTimeMillis() + 1000000);
        List<Loan> loans = List.of(loan);
        when(loanRepository.findByLoanDateTimeBetween(from, to)).thenReturn(loans);

        List<Loan> result = serviceLoan.listLoansByDateRange(from, to);

        assertEquals(1, result.size());
    }

    // --- sendReturnReminder tests ---
    @Test
    public void sendReturnReminder_SendsNotificationIfClose() {
        loan.setReturnDueDateTime(LocalDateTime.now().plusMinutes(3));
        loan.setReturned(false);
        when(loanRepository.findLoanById("loan1")).thenReturn(loan);

        // No exception, should print notification (check console output manually or mock system out)
        serviceLoan.sendReturnReminder("loan1");
    }

    @Test
    public void sendReturnReminder_DoesNotSendIfReturned() {
        loan.setReturned(true);
        when(loanRepository.findLoanById("loan1")).thenReturn(loan);

        serviceLoan.sendReturnReminder("loan1"); // No exception, no output expected
    }

    @Test
    public void sendReturnReminder_ThrowsIfLoanNotFound() {
        when(loanRepository.findLoanById("loan1")).thenReturn(null);

        SportLoanException.LoanNotFoundException ex = assertThrows(
                SportLoanException.LoanNotFoundException.class,
                () -> serviceLoan.sendReturnReminder("loan1"));
        assertTrue(ex.getMessage().contains("Loan with ID loan1 not found."));
    }

    // --- sendReturnOutReminder tests ---
    @Test
    public void sendReturnOutReminder_SendsNotificationIfOverdue() {
        loan.setReturnDueDateTime(LocalDateTime.now().minusMinutes(1));
        when(loanRepository.findLoanById("loan1")).thenReturn(loan);

        serviceLoan.sendReturnOutReminder("loan1"); // Should print notification
    }

    @Test
    public void sendReturnOutReminder_DoesNothingIfNotOverdue() {
        loan.setReturnDueDateTime(LocalDateTime.now().plusMinutes(10));
        when(loanRepository.findLoanById("loan1")).thenReturn(loan);

        serviceLoan.sendReturnOutReminder("loan1"); // No exception, no output expected
    }

    @Test
    public void sendReturnOutReminder_ThrowsIfLoanNotFound() {
        when(loanRepository.findLoanById("loan1")).thenReturn(null);

        SportLoanException.LoanNotFoundException ex = assertThrows(
                SportLoanException.LoanNotFoundException.class,
                () -> serviceLoan.sendReturnOutReminder("loan1"));
        assertTrue(ex.getMessage().contains("Loan with ID loan1 not found."));
    }

    // --- checkLoansAndSendReminders tests ---
    @Test
    public void checkLoansAndSendReminders_CallsSenders() {
        List<Loan> activeLoans = List.of(loan);
        when(loanRepository.findAllByReturnedFalse()).thenReturn(activeLoans);
        when(loanRepository.findLoanById("loan1")).thenReturn(loan);

        // Just call the scheduled method, it should invoke the reminders
        serviceLoan.checkLoansAndSendReminders();

        verify(loanRepository, times(1)).findAllByReturnedFalse();
    }
}

