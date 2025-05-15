package eci.cvds.tdd.module.sportLoan.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testAllEnumValues() {
        Role[] roles = Role.values();
        assertEquals(4, roles.length);
        assertArrayEquals(new Role[]{
                Role.STUDENT,
                Role.TEACHER,
                Role.ADMIN,
                Role.GENERALSERVICES
        }, roles);
    }

    @Test
    void testValueOf() {
        assertEquals(Role.STUDENT, Role.valueOf("STUDENT"));
        assertEquals(Role.TEACHER, Role.valueOf("TEACHER"));
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.GENERALSERVICES, Role.valueOf("GENERALSERVICES"));
    }
}
