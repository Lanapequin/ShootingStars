package eci.cvds.tdd.module.sportLoan.service;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import eci.cvds.tdd.module.sportLoan.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de la gestión de equipos deportivos.
 * Permite agregar, actualizar estado, habilitar, deshabilitar y consultar equipos.
 */
@Service
public class ServiceEquipment implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    /**
     * Agrega un nuevo equipo al sistema, estableciendo su disponibilidad como verdadera
     * y su estado como "GOODSTATUS".
     *
     * @param equipment El equipo a agregar.
     * @return El equipo guardado en el repositorio.
     */
    @Override
    public Equipment addEquipment(Equipment equipment) {
        equipment.setAvailable(true);
        equipment.setStatus(EquipmentStatus.GOODSTATUS);
        return equipmentRepository.save(equipment);
    }

    /**
     * Actualiza el estado de un equipo específico.
     *
     * @param request Contiene el ID del equipo y el nuevo estado a asignar.
     * @return El equipo con el estado actualizado.
     * @throws RuntimeException Si el equipo no se encuentra.
     */
    @Override
    public Equipment updateEquipmentStatus(EquipmentStatusUpdateRequest request) {
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setStatus(request.getNewStatus());
        return equipmentRepository.save(equipment);
    }

    /**
     * Retorna una lista de equipos disponibles que están en buen estado.
     *
     * @return Lista de equipos disponibles con estado GOODSTATUS.
     */
    @Override
    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByStatusAndAvailable(EquipmentStatus.GOODSTATUS, true);
    }

    /**
     * Deshabilita un equipo, marcándolo como no disponible.
     *
     * @param equipmentId ID del equipo a deshabilitar.
     * @throws RuntimeException Si el equipo no se encuentra.
     */
    @Override
    public void disableEquipment(String equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setAvailable(false);
        equipmentRepository.save(equipment);
    }

    /**
     * Habilita un equipo, marcándolo como disponible.
     *
     * @param equipmentId ID del equipo a habilitar.
     * @throws RuntimeException Si el equipo no se encuentra.
     */
    @Override
    public void enableEquipment(String equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setAvailable(true);
        equipmentRepository.save(equipment);
    }

    /**
     * Consulta un equipo por su ID.
     *
     * @param equipmentId ID del equipo a buscar.
     * @return El equipo encontrado.
     * @throws RuntimeException Si el equipo no se encuentra.
     */
    @Override
    public Equipment getEquipmentById(String equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
    }
}
