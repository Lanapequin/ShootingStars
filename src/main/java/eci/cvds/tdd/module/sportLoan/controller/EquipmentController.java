package eci.cvds.tdd.module.sportLoan.controller;


import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador encargado de la gestión de equipos deportivos.
 * Proporciona endpoints para agregar, actualizar estado, habilitar, deshabilitar y consultar equipos.
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * Agrega un nuevo equipo al sistema.
     *
     * @param equipment El equipo a agregar.
     * @return ResponseEntity con el equipo guardado.
     */
    @PostMapping
    public ResponseEntity<Equipment> addEquipment(@RequestBody Equipment equipment) {
        return ResponseEntity.ok(equipmentService.addEquipment(equipment));
    }

    /**
     * Actualiza el estado de un equipo específico.
     *
     * @param request Contiene el ID del equipo y el nuevo estado a asignar.
     * @return ResponseEntity con el equipo actualizado.
     */
    @PutMapping("/status")
    public ResponseEntity<Equipment> updateStatus(@RequestBody EquipmentStatusUpdateRequest request) {
        return ResponseEntity.ok(equipmentService.updateEquipmentStatus(request));
    }

    /**
     * Obtiene una lista de equipos disponibles en buen estado.
     *
     * @return ResponseEntity con la lista de equipos disponibles.
     */
    @GetMapping("/available")
    public ResponseEntity<List<Equipment>> getAvailableEquipment() {
        return ResponseEntity.ok(equipmentService.getAvailableEquipment());
    }

    /**
     * Deshabilita un equipo, marcándolo como no disponible.
     *
     * @param id ID del equipo a deshabilitar.
     * @return ResponseEntity vacío con estado HTTP OK.
     */
    @PutMapping("/disable/{id}")
    public ResponseEntity<Void> disableEquipment(@PathVariable String id) {
        equipmentService.disableEquipment(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Habilita un equipo, marcándolo como disponible.
     *
     * @param id ID del equipo a habilitar.
     * @return ResponseEntity vacío con estado HTTP OK.
     */
    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enableEquipment(@PathVariable String id) {
        equipmentService.enableEquipment(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Consulta un equipo por su ID.
     *
     * @param id ID del equipo a buscar.
     * @return ResponseEntity con el equipo encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable String id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @GetMapping("/getBadEquipment")
    public ResponseEntity<List<Equipment>> getEquipmentBadAndMaintenance() {
        return ResponseEntity.ok(equipmentService.getBadAndMaintenance());
    }


}