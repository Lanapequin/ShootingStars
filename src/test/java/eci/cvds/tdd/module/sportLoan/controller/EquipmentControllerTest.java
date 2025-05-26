package eci.cvds.tdd.module.sportLoan.controller;

import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.service.EquipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipmentControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEquipment() {
        Equipment equipment = new Equipment();
        when(equipmentService.addEquipment(equipment)).thenReturn(equipment);

        ResponseEntity<Equipment> response = equipmentController.addEquipment(equipment, "Bearer token");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(equipment, response.getBody());
    }

    @Test
    void testUpdateStatus() {
        EquipmentStatusUpdateRequest request = new EquipmentStatusUpdateRequest();
        Equipment equipment = new Equipment();
        when(equipmentService.updateEquipmentStatus(request)).thenReturn(equipment);

        ResponseEntity<Equipment> response = equipmentController.updateStatus(request, "Bearer token");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(equipment, response.getBody());
    }

    @Test
    void testGetAvailableEquipment() {
        List<Equipment> available = Arrays.asList(new Equipment(), new Equipment());
        when(equipmentService.getAvailableEquipment()).thenReturn(available);

        ResponseEntity<List<Equipment>> response = equipmentController.getAvailableEquipment("Bearer token");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(available, response.getBody());
    }

    @Test
    void testDisableEquipment() {
        String id = "123";
        doNothing().when(equipmentService).disableEquipment(id);

        ResponseEntity<Void> response = equipmentController.disableEquipment(id, "Bearer token");

        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).disableEquipment(id);
    }

    @Test
    void testEnableEquipment() {
        String id = "123";
        doNothing().when(equipmentService).enableEquipment(id);

        ResponseEntity<Void> response = equipmentController.enableEquipment(id, "Bearer token");

        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).enableEquipment(id);
    }

    @Test
    void testGetEquipmentById() {
        String id = "456";
        Equipment equipment = new Equipment();
        when(equipmentService.getEquipmentById(id)).thenReturn(equipment);

        ResponseEntity<Equipment> response = equipmentController.getEquipmentById(id, "Bearer token");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(equipment, response.getBody());
    }

    @Test
    void testGetBadAndMaintenance() {
        List<Equipment> list = Arrays.asList(new Equipment());
        when(equipmentService.getBadAndMaintenance()).thenReturn(list);

        ResponseEntity<List<Equipment>> response = equipmentController.getEquipmentBadAndMaintenance("Bearer token");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody());
    }
}
