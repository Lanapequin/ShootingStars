package eci.cvds.tdd.module.sportLoan.model.DTO;
import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Data
public class ReturnDetails {
    private String loanId;
    private EquipmentStatus returnStatus;
    private String confirmedReturnBy;
    private String observations;
}


