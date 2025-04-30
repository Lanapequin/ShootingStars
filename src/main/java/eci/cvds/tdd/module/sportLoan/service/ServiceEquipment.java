package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;

import java.util.List;

public class ServiceEquipment implements EquipmentService{

    @Override
    public Equipment addEquipment(Equipment equipment) {
        return null;
    }

    @Override
    public Equipment updateEquipmentStatus(EquipmentStatusUpdateRequest request) {
        return null;
    }

    @Override
    public List<Equipment> getAvailableEquipment() {
        return List.of();
    }

    @Override
    public void disableEquipment(String equipmentId) {

    }

    @Override
    public void enableEquipment(String equipmentId) {

    }

    @Override
    public Equipment getEquipmentById(String equipmentId) {
        return null;
    }
}
