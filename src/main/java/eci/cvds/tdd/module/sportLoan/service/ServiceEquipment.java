package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceEquipment implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public Equipment addEquipment(Equipment equipment) {
        equipment.setAvailable(true);
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment updateEquipmentStatus(EquipmentStatusUpdateRequest request) {
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setStatus(request.getNewStatus());
        return equipmentRepository.save(equipment);
    }

    @Override
    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByStatusAndAvailable(EquipmentStatus.AVAILABLE, true);
    }

    @Override
    public void disableEquipment(String equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setAvailable(false);
        equipmentRepository.save(equipment);
    }

    @Override
    public void enableEquipment(String equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setAvailable(true);
        equipmentRepository.save(equipment);
    }

    @Override
    public Equipment getEquipmentById(String equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
    }
}