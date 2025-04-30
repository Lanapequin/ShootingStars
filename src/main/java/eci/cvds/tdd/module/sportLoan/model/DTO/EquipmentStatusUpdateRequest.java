package eci.cvds.tdd.module.sportLoan.model.DTO;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import lombok.Data;

@Data
public class EquipmentStatusUpdateRequest {
    private String equipmentId;
    private EquipmentStatus newStatus;
}
