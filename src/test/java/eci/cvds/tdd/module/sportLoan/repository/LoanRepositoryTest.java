package eci.cvds.tdd.module.sportLoan.repository;

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
class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @BeforeEach
    void cleanDatabase() {
        loanRepository.deleteAll();
    }

    private Loan createLoan(String userId, boolean returned, LocalDateTime loanDate, LocalDateTime returnDate) {
        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setReturned(returned);
        loan.setLoanDateTime(loanDate);
        loan.setReturnDueDateTime(returnDate);
        return loanRepository.save(loan);
    }

    @Test
    void testFindAllByReturnedFalse() {
        createLoan("user1", false, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        createLoan("user2", true, LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        List<Loan> result = loanRepository.findAllByReturnedFalse();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isReturned());
    }

    @Test
    void testFindByUserId() {
        Loan saved = createLoan("juanito", false, LocalDateTime.now(), LocalDateTime.now().plusHours(3));

        List<Loan> loans = loanRepository.findByUserId("juanito");

        assertEquals(1, loans.size());
        assertEquals("juanito", loans.get(0).getUserId());
    }

    @Test
    void testFindLoanById() {
        Loan saved = createLoan("test", false, LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        Loan found = loanRepository.findLoanById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void testFindByLoanDateTimeBetween() {
        LocalDateTime now = LocalDateTime.now();
        createLoan("rangeTest", false, now.minusDays(1), now.plusHours(1));

        Date from = Date.from(now.minusDays(2).atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(now.plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

        List<Loan> result = loanRepository.findByLoanDateTimeBetween(from, to);

        assertEquals(1, result.size());
        assertEquals("rangeTest", result.get(0).getUserId());
    }

    @Test
    void testFindAllByUserIdAndReturnedFalse() {
        createLoan("pepe", false, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        createLoan("pepe", true, LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        List<Loan> result = loanRepository.findAllByUserIdAndReturnedFalse("pepe");

        assertEquals(1, result.size());
        assertEquals("pepe", result.get(0).getUserId());
        assertFalse(result.get(0).isReturned());
    }
}
