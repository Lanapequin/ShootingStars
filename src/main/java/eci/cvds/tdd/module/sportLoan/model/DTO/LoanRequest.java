package eci.cvds.tdd.module.sportLoan.model.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Representación de una solicitud de préstamo de equipo deportivo.
 * Contiene la información necesaria para registrar un nuevo préstamo en el sistema.
 */
@Data
public class LoanRequest {
    private String userId;
    private String equipmentId;
    private LocalDateTime loanDateTime;
    private LocalDateTime returnDueDateTime;
    private int loanDurationHours;
}
