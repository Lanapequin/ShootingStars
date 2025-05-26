package eci.cvds.tdd.module.sportLoan.enums;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EquipmentStatusTest {

    @Test
    public void testValues() {
        EquipmentStatus[] statuses = EquipmentStatus.values();
        assertEquals(3, statuses.length);
        assertArrayEquals(
                new EquipmentStatus[]{EquipmentStatus.GOODSTATUS, EquipmentStatus.DAMAGED, EquipmentStatus.MAINTENANCE},
                statuses
        );
    }

    @Test
    public void testValueOf() {
        assertEquals(EquipmentStatus.DAMAGED, EquipmentStatus.valueOf("DAMAGED"));
        assertEquals(EquipmentStatus.MAINTENANCE, EquipmentStatus.valueOf("MAINTENANCE"));
        assertEquals(EquipmentStatus.GOODSTATUS, EquipmentStatus.valueOf("GOODSTATUS"));
    }
}
