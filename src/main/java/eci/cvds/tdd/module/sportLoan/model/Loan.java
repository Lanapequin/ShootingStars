package eci.cvds.tdd.module.sportLoan.model;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Document(collection = "Loan")
public class Loan {
    @Id
    private String id;
    private String userId;
    private List<String> equipmentIds;
    private Date loanDateTime;
    private Date returnDueDateTime;
    private Date returnDateTime;
    private int loanDurationHours;
    private Map<String, EquipmentStatus> initialEquipmentStatus;
    private Map<String, EquipmentStatus> returnEquipmentStatus;
    private String observations;
    private String confirmedReturnBy;
}
