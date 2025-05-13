package eci.cvds.tdd.module.sportLoan.model.DTO;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import lombok.Data;

/**
 * Representación de una solicitud de actualización de estado de un equipo deportivo.
 * Se utiliza para modificar el estado de un equipo dentro del sistema.
 */
@Data
public class EquipmentStatusUpdateRequest {
    private String equipmentId;
    private EquipmentStatus newStatus;
}