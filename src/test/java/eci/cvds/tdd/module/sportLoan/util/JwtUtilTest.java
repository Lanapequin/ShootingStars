package eci.cvds.tdd.module.sportLoan.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String validAdminToken;
    private String validUserToken;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        Algorithm algorithm = Algorithm.HMAC256("secret");

        validAdminToken = "Bearer " + JWT.create()
                .withClaim("id", "admin123")
                .withClaim("role", "ADMIN")
                .sign(algorithm);

        validUserToken = "Bearer " + JWT.create()
                .withClaim("id", "user456")
                .withClaim("role", "STUDENT")
                .sign(algorithm);
    }

    @Test
    void testValidateAdminWithAdminToken() {
        assertTrue(jwtUtil.validateAdmin(validAdminToken));
    }

    @Test
    void testValidateAdminWithNonAdminToken() {
        assertFalse(jwtUtil.validateAdmin(validUserToken));
    }

    @Test
    void testGetIdFromToken() {
        String id = jwtUtil.getIdFromToken(validUserToken);
        assertEquals("user456", id);
    }

    @Test
    void testValidateAdminWithInvalidToken() {
        String invalidToken = "Bearer invalid.token.here";
        assertFalse(jwtUtil.validateAdmin(invalidToken));
    }
}

