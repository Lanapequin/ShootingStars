package eci.cvds.tdd.module.sportLoan.service;


import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;

import java.util.List;

public interface EquipmentService {
    Equipment addEquipment(Equipment equipment);
    Equipment updateEquipmentStatus(EquipmentStatusUpdateRequest request);
    List<Equipment> getAvailableEquipment();
    void disableEquipment(String equipmentId);
    void enableEquipment(String equipmentId);
    Equipment getEquipmentById(String equipmentId);
}