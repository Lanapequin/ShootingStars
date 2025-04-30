package eci.cvds.tdd.module.sportLoan.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Document(collection = "equipment")
public class Equipment {
    @Id
    private String id;
    private String name;
    private String description;
    private String status;
    private boolean avilable;// si esta en un prestamo
}
