package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.model.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByUserId(String userId);
    List<Loan> findByLoanDateTimeBetween(Date start, Date end);
}

