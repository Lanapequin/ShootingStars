package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.model.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByUserId(String userId);
    Loan findLoanById(String loanId);
    List<Loan> findByLoanDateTimeBetween(Date start, Date end);
    default List<Loan> findAllByReturnedFalse(){
        List<Loan> loans = findAll();
        List<Loan> falseloans= new ArrayList<>();
        for (Loan l: loans){
            if(!l.isReturned()){
                falseloans.add(l);
            }
        }
        return falseloans;
    }
}

