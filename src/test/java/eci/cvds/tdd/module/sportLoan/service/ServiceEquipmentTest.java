package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceEquipmentTest {

    @InjectMocks
    private ServiceEquipment serviceEquipment;

    @Mock
    private EquipmentRepository equipmentRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEquipment() {
        Equipment equipment = new Equipment();
        when(equipmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Equipment saved = serviceEquipment.addEquipment(equipment);

        assertTrue(saved.isAvailable());
        assertEquals(EquipmentStatus.GOODSTATUS, saved.getStatus());
        verify(equipmentRepository).save(saved);
    }

    @Test
    void testUpdateEquipmentStatusSuccess() {
        EquipmentStatusUpdateRequest req = new EquipmentStatusUpdateRequest();
        req.setEquipmentId("123");
        req.setNewStatus(EquipmentStatus.DAMAGED);

        Equipment existing = new Equipment();
        existing.setId("123");
        existing.setStatus(EquipmentStatus.GOODSTATUS);

        when(equipmentRepository.findById("123")).thenReturn(Optional.of(existing));
        when(equipmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Equipment updated = serviceEquipment.updateEquipmentStatus(req);

        assertEquals(EquipmentStatus.DAMAGED, updated.getStatus());
        verify(equipmentRepository).save(updated);
    }

    @Test
    void testUpdateEquipmentStatusThrowsWhenNotFound() {
        EquipmentStatusUpdateRequest req = new EquipmentStatusUpdateRequest();
        req.setEquipmentId("not-exist");
        req.setNewStatus(EquipmentStatus.DAMAGED);

        when(equipmentRepository.findById("not-exist")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                serviceEquipment.updateEquipmentStatus(req));
        assertEquals("Equipment not found", ex.getMessage());
    }

    @Test
    void testGetAvailableEquipment() {
        List<Equipment> list = List.of(new Equipment(), new Equipment());
        when(equipmentRepository.findByStatusAndAvailable(EquipmentStatus.GOODSTATUS, true)).thenReturn(list);

        List<Equipment> result = serviceEquipment.getAvailableEquipment();

        assertEquals(2, result.size());
    }

    @Test
    void testDisableEquipmentSuccess() {
        Equipment equipment = new Equipment();
        equipment.setAvailable(true);

        when(equipmentRepository.findById("x")).thenReturn(Optional.of(equipment));

        serviceEquipment.disableEquipment("x");

        assertFalse(equipment.isAvailable());
        verify(equipmentRepository).save(equipment);
    }

    @Test
    void testDisableEquipmentThrowsWhenNotFound() {
        when(equipmentRepository.findById("x")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                serviceEquipment.disableEquipment("x"));
        assertEquals("Equipment not found", ex.getMessage());
    }

    @Test
    void testEnableEquipmentSuccess() {
        Equipment equipment = new Equipment();
        equipment.setAvailable(false);

        when(equipmentRepository.findById("y")).thenReturn(Optional.of(equipment));

        serviceEquipment.enableEquipment("y");

        assertTrue(equipment.isAvailable());
        verify(equipmentRepository).save(equipment);
    }

    @Test
    void testEnableEquipmentThrowsWhenNotFound() {
        when(equipmentRepository.findById("y")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                serviceEquipment.enableEquipment("y"));
        assertEquals("Equipment not found", ex.getMessage());
    }

    @Test
    void testGetEquipmentByIdSuccess() {
        Equipment equipment = new Equipment();
        when(equipmentRepository.findById("123")).thenReturn(Optional.of(equipment));

        Equipment result = serviceEquipment.getEquipmentById("123");

        assertNotNull(result);
        assertEquals(equipment, result);
    }

    @Test
    void testGetEquipmentByIdThrowsWhenNotFound() {
        when(equipmentRepository.findById("123")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                serviceEquipment.getEquipmentById("123"));
        assertEquals("Equipment not found", ex.getMessage());
    }

    @Test
    void testGetBadAndMaintenance() {
        List<Equipment> list = List.of(new Equipment());
        when(equipmentRepository.findBadAndMaintenance()).thenReturn(list);

        List<Equipment> result = serviceEquipment.getBadAndMaintenance();

        assertEquals(1, result.size());
        verify(equipmentRepository).findBadAndMaintenance();
    }
}
