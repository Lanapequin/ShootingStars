package eci.cvds.tdd.module.sportLoan.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtRequestFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String SECRET = "secret";

    // Método para crear un token JWT válido para tests
    private String createValidToken() {
        return JWT.create()
                .withClaim("id", "testUser")
                .withClaim("role", "USER")
                .sign(Algorithm.HMAC256(SECRET));
    }

    @Test
    void requestWithInvalidToken_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/equipment/available")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void requestWithValidToken_shouldAllowAccess() throws Exception {
        String token = createValidToken();
        mockMvc.perform(get("/api/equipment/available")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
