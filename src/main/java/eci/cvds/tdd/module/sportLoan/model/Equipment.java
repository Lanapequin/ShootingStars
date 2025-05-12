package eci.cvds.tdd.module.sportLoan.model;

import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Equipment")
public class Equipment {
    @Id
    private String id;
    private String name;
    private String description;
    private EquipmentStatus status;
    private String observations;
    private boolean available;

    public boolean isAvailable() {
        return available;
    }
}