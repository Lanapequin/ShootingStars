package eci.cvds.tdd.module.sportLoan.model;

import eci.cvds.tdd.module.sportLoan.enums.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "User")
public class User {
    private Long id;
    private List<Loan> loans;
    private Role role;
}

