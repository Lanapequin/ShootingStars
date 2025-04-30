package eci.cvds.tdd.module.sportLoan.controller;

import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public Loan createLoan(@RequestBody LoanRequest request) {
        return loanService.createLoan(request);
    }

    @PostMapping("/return")
    public Loan returnLoan(@RequestBody ReturnDetails details) {
        return loanService.returnLoan(details);
    }

    @DeleteMapping("/{loanId}")
    public void cancelLoan(@PathVariable String loanId) {
        loanService.cancelLoan(loanId);
    }

    @GetMapping("/{loanId}")
    public Loan getLoan(@PathVariable String loanId) {
        return loanService.getLoanById(loanId);
    }

    @GetMapping("/user/{userId}")
    public List<Loan> listLoansByUser(@PathVariable String userId) {
        return loanService.listLoansByUser(userId);
    }

    @GetMapping("/range")
    public List<Loan> listLoansByDateRange(@RequestParam Date from, @RequestParam Date to) {
        return loanService.listLoansByDateRange(from, to);
    }
}
