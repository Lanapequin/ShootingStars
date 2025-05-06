package eci.cvds.tdd.module.sportLoan.model;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "Loan")
public class Loan {
    @Id
    private String id;
    private String userId;
    private String equipmentId;
    private LocalDateTime loanDateTime;
    private LocalDateTime returnDueDateTime;
    private LocalDateTime returnDateTime;
    private int loanDurationHours;
    private EquipmentStatus initialEquipmentStatus;
    private EquipmentStatus returnEquipmentStatus;
    private String observations;
    private String confirmedReturnBy;
}
