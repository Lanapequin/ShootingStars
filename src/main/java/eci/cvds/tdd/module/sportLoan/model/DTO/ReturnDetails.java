package eci.cvds.tdd.module.sportLoan.model.DTO;
import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import lombok.Data;

import java.util.Map;

@Data
public class ReturnDetails {
    private String loanId;
    private Map<String, EquipmentStatus> equipmentStatusUpdate;
    private String observations;
    private String confirmedByFuncionarioId;
}

