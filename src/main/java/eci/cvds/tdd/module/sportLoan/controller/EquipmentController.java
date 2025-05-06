package eci.cvds.tdd.module.sportLoan.controller;


import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<Equipment> addEquipment(@RequestBody Equipment equipment) {
        return ResponseEntity.ok(equipmentService.addEquipment(equipment));
    }

    @PutMapping("/status")
    public ResponseEntity<Equipment> updateStatus(@RequestBody EquipmentStatusUpdateRequest request) {
        return ResponseEntity.ok(equipmentService.updateEquipmentStatus(request));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Equipment>> getAvailableEquipment() {
        return ResponseEntity.ok(equipmentService.getAvailableEquipment());
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Void> disableEquipment(@PathVariable String id) {
        equipmentService.disableEquipment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enableEquipment(@PathVariable String id) {
        equipmentService.enableEquipment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable String id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }
}