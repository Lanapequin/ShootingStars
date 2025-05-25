//package eci.cvds.tdd.module.sportLoan.model;
//
//import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class EquipmentStatusTest {
//
//    @Test
//    void testEnumValues() {
//        EquipmentStatus[] values = EquipmentStatus.values();
//
//        assertEquals(3, values.length);
//        assertTrue(contains(values, EquipmentStatus.GOODSTATUS));
//        assertTrue(contains(values, EquipmentStatus.DAMAGED));
//        assertTrue(contains(values, EquipmentStatus.MAINTENANCE));
//    }
//
//    @Test
//    void testValueOf() {
//        assertEquals(EquipmentStatus.GOODSTATUS, EquipmentStatus.valueOf("GOODSTATUS"));
//        assertEquals(EquipmentStatus.DAMAGED, EquipmentStatus.valueOf("DAMAGED"));
//        assertEquals(EquipmentStatus.MAINTENANCE, EquipmentStatus.valueOf("MAINTENANCE"));
//    }
//
//    private boolean contains(EquipmentStatus[] values, EquipmentStatus target) {
//        for (EquipmentStatus status : values) {
//            if (status == target) return true;
//        }
//        return false;
//    }
//}
