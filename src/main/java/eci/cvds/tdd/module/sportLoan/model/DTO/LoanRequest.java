package eci.cvds.tdd.module.sportLoan.model.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class LoanRequest {
    private Long userId;
    private String equipmentId;
    private LocalDateTime loanDateTime;
    private LocalDateTime returnDueDateTime;
    private int loanDurationHours;
}
