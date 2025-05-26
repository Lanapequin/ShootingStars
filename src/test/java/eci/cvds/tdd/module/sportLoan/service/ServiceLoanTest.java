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
import eci.cvds.tdd.module.sportLoan.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceLoanTest {

    @InjectMocks
    private ServiceLoan serviceLoan;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private LoanRequest createValidRequest() {
        // Lunes 9am → horario válido
        LocalDateTime mondayMorning = LocalDateTime.now()
                .with(DayOfWeek.MONDAY)
                .withHour(9).withMinute(0).withSecond(0).withNano(0)
                .plusWeeks(1); // evitar que sea pasado

        LoanRequest req = new LoanRequest();
        req.setUserId("user1");
        req.setEquipmentId("equip1");
        req.setLoanDateTime(mondayMorning);
        req.setReturnDueDateTime(mondayMorning.plusHours(2));
        req.setLoanDurationHours(2);
        return req;
    }

    @Test
    void testCreateLoanWithEmptyUserId() {
        LoanRequest request = createValidRequest();
        request.setUserId("");
        assertThrows(IllegalArgumentException.class, () -> serviceLoan.createLoan(request));
    }

    @Test
    void testCreateLoanLoanAfterReturnDate() {
        LoanRequest request = createValidRequest();
        request.setReturnDueDateTime(request.getLoanDateTime().minusHours(1));
        assertThrows(IllegalArgumentException.class, () -> serviceLoan.createLoan(request));
    }

    @Test
    void testCreateLoanWithInvalidHorario() {
        LocalDateTime sundayEvening = LocalDateTime.now().with(DayOfWeek.SUNDAY).withHour(18).plusWeeks(1);
        LoanRequest request = new LoanRequest();
        request.setUserId("user1");
        request.setEquipmentId("equip1");
        request.setLoanDateTime(sundayEvening);
        request.setReturnDueDateTime(sundayEvening.plusHours(1));
        request.setLoanDurationHours(1);
        assertThrows(IllegalArgumentException.class, () -> serviceLoan.createLoan(request));
    }

    @Test
    void testCreateLoanWithUnavailableEquipment() {
        LoanRequest request = createValidRequest();
        Equipment equipment = new Equipment();
        equipment.setId("equip1");
        equipment.setAvailable(false);
        equipment.setStatus(EquipmentStatus.GOODSTATUS);
        when(equipmentRepository.findById("equip1")).thenReturn(Optional.of(equipment));
        assertThrows(SportLoanException.EquipmentNotAvailableException.class, () -> serviceLoan.createLoan(request));
    }

    @Test
    void testCreateLoanWithDamagedEquipment() {
        LoanRequest request = createValidRequest();
        Equipment equipment = new Equipment();
        equipment.setId("equip1");
        equipment.setAvailable(true);
        equipment.setStatus(EquipmentStatus.DAMAGED);
        when(equipmentRepository.findById("equip1")).thenReturn(Optional.of(equipment));
        assertThrows(SportLoanException.EquipmentNotAvailableException.class, () -> serviceLoan.createLoan(request));
    }

    @Test
    void testCreateLoanWithMaintenanceEquipment() {
        LoanRequest request = createValidRequest();
        Equipment equipment = new Equipment();
        equipment.setId("equip1");
        equipment.setAvailable(true);
        equipment.setStatus(EquipmentStatus.MAINTENANCE);
        when(equipmentRepository.findById("equip1")).thenReturn(Optional.of(equipment));
        assertThrows(SportLoanException.EquipmentNotAvailableException.class, () -> serviceLoan.createLoan(request));
    }

    @Test
    void testCreateLoanFailsOnPastDate() {
        LoanRequest request = createValidRequest();
        request.setLoanDateTime(LocalDateTime.now().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> serviceLoan.createLoan(request));
    }

    @Test
    void testAddLoanToNewUser() {
        Loan loan = new Loan();
        loan.setUserId("newUser");
        when(userRepository.existsById("newUser")).thenReturn(false);
        serviceLoan.addLoanToUser(loan);
        verify(userRepository).save(any());
    }

    @Test
    void testAddLoanToExistingUser() {
        Loan loan = new Loan();
        loan.setUserId("user1");
        User user = new User();
        user.setId("user1");
        user.setLoans(new ArrayList<>());
        when(userRepository.existsById("user1")).thenReturn(true);
        when(userRepository.findUserById("user1")).thenReturn(user);
        serviceLoan.addLoanToUser(loan);
        verify(userRepository).save(user);
    }

    @Test
    void testCancelLoan() {
        Loan loan = new Loan();
        loan.setEquipmentId("equip1");
        Equipment equipment = new Equipment();
        when(loanRepository.findById("loan1")).thenReturn(Optional.of(loan));
        when(equipmentRepository.findById("equip1")).thenReturn(Optional.of(equipment));
        serviceLoan.cancelLoan("loan1");
        assertEquals(EquipmentStatus.GOODSTATUS, equipment.getStatus());
        verify(loanRepository).deleteById("loan1");
    }

    @Test
    void testReturnLoanSuccessfully() {
        Loan loan = new Loan();
        loan.setId("l1");
        loan.setReturned(false);
        loan.setUserId("u1");
        loan.setEquipmentId("e1");
        Equipment equipment = new Equipment();
        equipment.setAvailable(false);
        User user = new User();
        user.setId("u1");
        user.setLoans(new ArrayList<>(List.of(loan)));
        ReturnDetails details = new ReturnDetails();
        details.setLoanId("l1");
        details.setReturnStatus(EquipmentStatus.GOODSTATUS);
        details.setObservations("Todo bien");
        details.setConfirmedReturnBy("admin");
        when(loanRepository.findById("l1")).thenReturn(Optional.of(loan));
        when(equipmentRepository.findById("e1")).thenReturn(Optional.of(equipment));
        when(userRepository.findUserById("u1")).thenReturn(user);
        when(loanRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Loan returned = serviceLoan.returnLoan(details);
        assertTrue(returned.isReturned());
        assertEquals("admin", returned.getConfirmedReturnBy());
    }

    @Test
    void testReturnLoanAlreadyReturned() {
        Loan loan = new Loan();
        loan.setReturned(true);
        ReturnDetails details = new ReturnDetails();
        details.setLoanId("123");
        when(loanRepository.findById("123")).thenReturn(Optional.of(loan));
        assertThrows(SportLoanException.LoanAlreadyReturnedException.class,
                () -> serviceLoan.returnLoan(details));
    }

    @Test
    void testSendReturnReminderUpcoming() {
        Loan loan = new Loan();
        loan.setId("l1");
        loan.setUserId("u1");
        loan.setEquipmentId("e1");
        loan.setReturned(false);
        loan.setReturnDueDateTime(LocalDateTime.now().plusMinutes(4));
        when(loanRepository.findLoanById("l1")).thenReturn(loan);
        serviceLoan.sendReturnReminder("l1");
    }

    @Test
    void testSendReturnReminderAlreadyReturned() {
        Loan loan = new Loan();
        loan.setReturned(true);
        when(loanRepository.findLoanById("x")).thenReturn(loan);
        serviceLoan.sendReturnReminder("x");
    }

    @Test
    void testSendReturnOutReminderExpired() {
        Loan loan = new Loan();
        loan.setUserId("u1");
        loan.setReturnDueDateTime(LocalDateTime.now().minusHours(1));
        when(loanRepository.findLoanById("x")).thenReturn(loan);
        serviceLoan.sendReturnOutReminder("x");
    }

    @Test
    void testGetNotificationsForUser() {
        Loan upcoming = new Loan();
        upcoming.setEquipmentId("e1");
        upcoming.setReturnDueDateTime(LocalDateTime.now().plusMinutes(4));
        Loan expired = new Loan();
        expired.setEquipmentId("e2");
        expired.setReturnDueDateTime(LocalDateTime.now().minusHours(1));
        when(loanRepository.findAllByUserIdAndReturnedFalse("u1"))
                .thenReturn(List.of(upcoming, expired));
        List<String> notifs = serviceLoan.getNotificationsForUser("u1");
        assertEquals(2, notifs.size());
    }

    @Test
    void testGetActiveLoans() {
        List<Loan> list = List.of(new Loan(), new Loan());
        when(loanRepository.findAllByReturnedFalse()).thenReturn(list);
        assertEquals(2, serviceLoan.getActiveLoans().size());
    }

    @Test
    void testGetLoanById() {
        Loan loan = new Loan();
        when(loanRepository.findById("123")).thenReturn(Optional.of(loan));
        assertEquals(loan, serviceLoan.getLoanById("123"));
    }

    @Test
    void testListLoansByUser() {
        when(loanRepository.findByUserId("u1")).thenReturn(List.of(new Loan()));
        assertEquals(1, serviceLoan.listLoansByUser("u1").size());
    }

    @Test
    void testListLoansByDateRange() {
        Date from = new Date();
        Date to = new Date();
        when(loanRepository.findByLoanDateTimeBetween(from, to)).thenReturn(List.of(new Loan()));
        assertEquals(1, serviceLoan.listLoansByDateRange(from, to).size());
    }

    @Test
    void testGetLoans() {
        when(loanRepository.findAll()).thenReturn(List.of(new Loan()));
        assertEquals(1, serviceLoan.getLoans().size());
    }
}
