package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    private Loan loan1, loan2, loan3;

    @BeforeEach
    public void setUp() {
        loanRepository.deleteAll(); // Limpieza

        loan1 = new Loan();
        loan1.setUserId("user1");
        loan1.setReturned(false);
        loan1.setLoanDateTime(LocalDateTime.of(2024, 5, 10, 10, 0));
        loanRepository.save(loan1);

        loan2 = new Loan();
        loan2.setUserId("user1");
        loan2.setReturned(true);
        loan2.setLoanDateTime(LocalDateTime.of(2024, 5, 15, 10, 0));
        loanRepository.save(loan2);

        loan3 = new Loan();
        loan3.setUserId("user2");
        loan3.setReturned(false);
        loan3.setLoanDateTime(LocalDateTime.of(2024, 6, 1, 10, 0));
        loanRepository.save(loan3);
    }

    @Test
    public void testFindByUserId() {
        List<Loan> loans = loanRepository.findByUserId("user1");
        assertEquals(2, loans.size());
    }

    @Test
    public void testFindLoanById() {
        Loan found = loanRepository.findLoanById(loan1.getId());
        assertNotNull(found);
        assertEquals(loan1.getUserId(), found.getUserId());
    }

    @Test
    public void testFindByLoanDateTimeBetween() {
        Date start = Date.from(LocalDateTime.of(2024, 5, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(LocalDateTime.of(2024, 5, 31, 23, 59).atZone(ZoneId.systemDefault()).toInstant());

        List<Loan> loans = loanRepository.findByLoanDateTimeBetween(start, end);
        assertEquals(2, loans.size()); // loan1 y loan2
    }

    @Test
    public void testFindAllByReturnedFalse() {
        List<Loan> activeLoans = loanRepository.findAllByReturnedFalse();
        assertEquals(2, activeLoans.size());
        assertTrue(activeLoans.stream().allMatch(l -> !l.isReturned()));
    }

    @Test
    public void testFindAllByUserIdAndReturnedFalse() {
        List<Loan> user1Active = loanRepository.findAllByUserIdAndReturnedFalse("user1");
        assertEquals(1, user1Active.size());
        assertFalse(user1Active.get(0).isReturned());
    }
}
