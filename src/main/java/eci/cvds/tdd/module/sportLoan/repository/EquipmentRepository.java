package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import eci.cvds.tdd.module.sportLoan.model.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repositorio de equipos deportivos.
 * Proporciona métodos para la gestión y consulta de equipos almacenados en la base de datos MongoDB.
 */
@Repository
public interface EquipmentRepository extends MongoRepository<Equipment, String> {

    /**
     * Obtiene una lista de equipos disponibles con un estado específico.
     *
     * @param status Estado del equipo (ejemplo: GOODSTATUS, DAMAGED, MAINTENANCE).
     * @param available Indica si el equipo está disponible para préstamo.
     * @return Lista de equipos que cumplen con los criterios de estado y disponibilidad.
     */
    List<Equipment> findByStatusAndAvailable(EquipmentStatus status, boolean available);
    default List<Equipment> findBadAndMaintenance(){
        List<Equipment> equipos = findAll();
        List<Equipment> badandmaintenance= new java.util.ArrayList<>();
        for (Equipment e: equipos){
            if(e.getStatus().equals(EquipmentStatus.DAMAGED) || e.getStatus().equals(EquipmentStatus.MAINTENANCE)){
                badandmaintenance.add(e);
            }
        }
        return badandmaintenance;
    }
}