package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @BeforeEach
    void cleanDatabase() {
        equipmentRepository.deleteAll();
    }

    private Equipment createEquipment(String id, EquipmentStatus status, boolean available) {
        Equipment equipment = new Equipment();
        equipment.setId(id);
        equipment.setStatus(status);
        equipment.setAvailable(available);
        return equipmentRepository.save(equipment);
    }

    @Test
    void testFindByStatusAndAvailable() {
        createEquipment("e1", EquipmentStatus.GOODSTATUS, true);
        createEquipment("e2", EquipmentStatus.GOODSTATUS, false);
        createEquipment("e3", EquipmentStatus.DAMAGED, true);

        List<Equipment> result = equipmentRepository.findByStatusAndAvailable(EquipmentStatus.GOODSTATUS, true);

        assertEquals(1, result.size());
        assertEquals("e1", result.get(0).getId());
        assertTrue(result.get(0).isAvailable());
    }

    @Test
    void testFindBadAndMaintenance() {
        createEquipment("e1", EquipmentStatus.GOODSTATUS, true);
        createEquipment("e2", EquipmentStatus.DAMAGED, false);
        createEquipment("e3", EquipmentStatus.MAINTENANCE, true);

        List<Equipment> result = equipmentRepository.findBadAndMaintenance();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(e -> e.getStatus() == EquipmentStatus.DAMAGED));
        assertTrue(result.stream().anyMatch(e -> e.getStatus() == EquipmentStatus.MAINTENANCE));
    }
}
