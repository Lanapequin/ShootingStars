package eci.cvds.tdd.module.sportLoan.controller;

import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.service.EquipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipmentControllerTest {

    @InjectMocks
    private EquipmentController equipmentController;

    @Mock
    private EquipmentService equipmentService;

    private final String dummyToken = "Bearer sometoken";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addEquipment_ReturnsSavedEquipment() {
        Equipment equipment = new Equipment();
        when(equipmentService.addEquipment(equipment)).thenReturn(equipment);

        ResponseEntity<Equipment> response = equipmentController.addEquipment(equipment, dummyToken);

        assertEquals(equipment, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).addEquipment(equipment);
    }

    @Test
    void updateStatus_ReturnsUpdatedEquipment() {
        EquipmentStatusUpdateRequest request = new EquipmentStatusUpdateRequest();
        Equipment equipment = new Equipment();
        when(equipmentService.updateEquipmentStatus(request)).thenReturn(equipment);

        ResponseEntity<Equipment> response = equipmentController.updateStatus(request, dummyToken);

        assertEquals(equipment, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).updateEquipmentStatus(request);
    }

    @Test
    void getAvailableEquipment_ReturnsList() {
        List<Equipment> equipmentList = List.of(new Equipment(), new Equipment());
        when(equipmentService.getAvailableEquipment()).thenReturn(equipmentList);

        ResponseEntity<List<Equipment>> response = equipmentController.getAvailableEquipment(dummyToken);

        assertEquals(equipmentList, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).getAvailableEquipment();
    }

    @Test
    void disableEquipment_ReturnsOk() {
        String id = "123";

        ResponseEntity<Void> response = equipmentController.disableEquipment(id, dummyToken);

        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).disableEquipment(id);
    }

    @Test
    void enableEquipment_ReturnsOk() {
        String id = "123";

        ResponseEntity<Void> response = equipmentController.enableEquipment(id, dummyToken);

        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).enableEquipment(id);
    }

    @Test
    void getEquipmentById_ReturnsEquipment() {
        String id = "123";
        Equipment equipment = new Equipment();
        when(equipmentService.getEquipmentById(id)).thenReturn(equipment);

        ResponseEntity<Equipment> response = equipmentController.getEquipmentById(id, dummyToken);

        assertEquals(equipment, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).getEquipmentById(id);
    }

    @Test
    void getEquipmentBadAndMaintenance_ReturnsList() {
        List<Equipment> equipmentList = List.of(new Equipment());
        when(equipmentService.getBadAndMaintenance()).thenReturn(equipmentList);

        ResponseEntity<List<Equipment>> response = equipmentController.getEquipmentBadAndMaintenance(dummyToken);

        assertEquals(equipmentList, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(equipmentService).getBadAndMaintenance();
    }
}
