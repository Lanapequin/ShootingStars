package eci.cvds.tdd.module.sportLoan.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateValidatorTest {

    @Test
    void testHorarioPermitidoLunesEnRango() {
        LocalDateTime lunes = LocalDateTime.of(2025, 5, 26, 9, 0); // Lunes 09:00
        assertTrue(DateValidator.esHorarioPermitido(lunes));
    }

    @Test
    void testHorarioFueraDeRangoLunesAntes() {
        LocalDateTime lunesTemprano = LocalDateTime.of(2025, 5, 26, 6, 59);
        assertFalse(DateValidator.esHorarioPermitido(lunesTemprano));
    }

    @Test
    void testHorarioPermitidoSabadoEnRango() {
        LocalDateTime sabado = LocalDateTime.of(2025, 5, 31, 9, 0); // Sábado 09:00
        assertTrue(DateValidator.esHorarioPermitido(sabado));
    }

    @Test
    void testHorarioFueraDeRangoSabadoTarde() {
        LocalDateTime sabadoTarde = LocalDateTime.of(2025, 5, 31, 13, 0);
        assertFalse(DateValidator.esHorarioPermitido(sabadoTarde));
    }

    @Test
    void testDomingoNoPermitido() {
        LocalDateTime domingo = LocalDateTime.of(2025, 6, 1, 10, 0);
        assertFalse(DateValidator.esHorarioPermitido(domingo));
    }
}
