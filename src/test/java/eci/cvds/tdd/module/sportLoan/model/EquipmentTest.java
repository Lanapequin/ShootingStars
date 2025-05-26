package eci.cvds.tdd.module.sportLoan.model;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EquipmentTest {

    @Test
    public void testEquipmentCreationAndAccessors() {
        Equipment equipment = new Equipment();
        equipment.setId("EQ001");
        equipment.setName("Balón de voleibol");
        equipment.setDescription("Balón Mikasa para interiores");
        equipment.setStatus(EquipmentStatus.GOODSTATUS);
        equipment.setObservations("Sin marcas visibles");
        equipment.setAvailable(true);

        assertEquals("EQ001", equipment.getId());
        assertEquals("Balón de voleibol", equipment.getName());
        assertEquals("Balón Mikasa para interiores", equipment.getDescription());
        assertEquals(EquipmentStatus.GOODSTATUS, equipment.getStatus());
        assertEquals("Sin marcas visibles", equipment.getObservations());
        assertTrue(equipment.isAvailable());
    }

    @Test
    public void testEquipmentAvailabilitySetterAndGetter() {
        Equipment equipment = new Equipment();
        equipment.setAvailable(false);
        assertFalse(equipment.isAvailable());

        equipment.setAvailable(true);
        assertTrue(equipment.isAvailable());
    }
}
