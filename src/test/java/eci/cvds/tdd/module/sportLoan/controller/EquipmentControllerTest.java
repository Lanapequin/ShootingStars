package eci.cvds.tdd.module.sportLoan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.service.EquipmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipmentController.class)
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddEquipment() throws Exception {
        Equipment equipment = new Equipment();
        equipment.setId("eq1");
        equipment.setName("Balón");

        Mockito.when(equipmentService.addEquipment(any())).thenReturn(equipment);

        mockMvc.perform(post("/api/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("eq1"));
    }

    @Test
    void testUpdateStatus() throws Exception {
        EquipmentStatusUpdateRequest request = new EquipmentStatusUpdateRequest();
        request.setEquipmentId("eq2");
        request.setNewStatus(EquipmentStatus.DAMAGED);

        Equipment updatedEquipment = new Equipment();
        updatedEquipment.setId("eq2");
        updatedEquipment.setStatus(EquipmentStatus.DAMAGED);

        Mockito.when(equipmentService.updateEquipmentStatus(any())).thenReturn(updatedEquipment);

        mockMvc.perform(put("/api/equipment/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DAMAGED"));
    }

    @Test
    void testGetAvailableEquipment() throws Exception {
        Equipment e1 = new Equipment(); e1.setId("e1"); e1.setAvailable(true);
        Equipment e2 = new Equipment(); e2.setId("e2"); e2.setAvailable(true);

        Mockito.when(equipmentService.getAvailableEquipment()).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/api/equipment/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testDisableEquipment() throws Exception {
        mockMvc.perform(put("/api/equipment/disable/eq3"))
                .andExpect(status().isOk());

        Mockito.verify(equipmentService).disableEquipment("eq3");
    }

    @Test
    void testEnableEquipment() throws Exception {
        mockMvc.perform(put("/api/equipment/enable/eq4"))
                .andExpect(status().isOk());

        Mockito.verify(equipmentService).enableEquipment("eq4");
    }

    @Test
    void testGetEquipmentById() throws Exception {
        Equipment equipment = new Equipment();
        equipment.setId("eq5");

        Mockito.when(equipmentService.getEquipmentById("eq5")).thenReturn(equipment);

        mockMvc.perform(get("/api/equipment/eq5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("eq5"));
    }
}