package eci.cvds.tdd.module.sportLoan.model.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoanRequest {
    private String userId;
    private List<String> equipmentIds;
    private Date loanDateTime;
    private Date returnDueDateTime;
}
