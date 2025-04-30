package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.model.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EquipmentRepository extends MongoRepository<Equipment, String> {

    @Query("{ 'name' : ?0 }")
    Equipment findByName(String name);

    default Equipment findByLoanDateTimeBetween(Date start, Date end) {
        return findByLoanDateTimeBetween(start, end);
    }

    default Equipment findById(String id) {
        return findById(id).orElse(null);
    }





}