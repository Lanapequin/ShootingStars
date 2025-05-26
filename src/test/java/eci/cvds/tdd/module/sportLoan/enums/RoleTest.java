package eci.cvds.tdd.module.sportLoan.enums;

import eci.cvds.tdd.module.sportLoan.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void testValues() {
        Role[] roles = Role.values();
        assertEquals(4, roles.length);
        assertArrayEquals(
                new Role[]{Role.STUDENT, Role.TEACHER, Role.ADMIN, Role.GENERALSERVICES},
                roles
        );
    }

    @Test
    public void testValueOf() {
        assertEquals(Role.STUDENT, Role.valueOf("STUDENT"));
        assertEquals(Role.TEACHER, Role.valueOf("TEACHER"));
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.GENERALSERVICES, Role.valueOf("GENERALSERVICES"));
    }
}
