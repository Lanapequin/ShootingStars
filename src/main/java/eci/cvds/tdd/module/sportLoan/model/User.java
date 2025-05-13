package eci.cvds.tdd.module.sportLoan.model;

import eci.cvds.tdd.module.sportLoan.enums.Role;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * Representación de un usuario dentro del sistema de préstamos deportivos.
 * Contiene información sobre los préstamos asociados y el rol del usuario.
 */
@Data
@Document(collection = "User")
public class User {
    private Long id;
    private List<Loan> loans;
    private Role role;
}