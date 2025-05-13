package eci.cvds.tdd.module.sportLoan.controller;

import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/create")
    public ResponseEntity<?> createLoan(@RequestBody LoanRequest request) {
        try {
            Loan loan = loanService.createLoan(request);
            loanService.addLoanToUser(loan);
            loanService.sendReturnReminder(loan.getId());
            return new ResponseEntity<>(loan, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Loan> returnLoan(@RequestBody ReturnDetails details) {
        Loan loan=loanService.returnLoan(details);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable("id") String loanId) {
        Loan loan = loanService.getLoanById(loanId);
        if (loan != null) {
            return new ResponseEntity<>(loan, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> listLoansByUser(@PathVariable String userId) {
        return new ResponseEntity<>(loanService.listLoansByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/range")
    public ResponseEntity<List<Loan>> listLoansByDateRange(@RequestParam Date from, @RequestParam Date to) {
        return new ResponseEntity<>(loanService.listLoansByDateRange(from, to), HttpStatus.OK);
    }
}