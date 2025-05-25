package eci.cvds.tdd.module.sportLoan.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateValidator {
    /**
     * Verifica si una fecha-hora está dentro del horario permitido:
     * - Lunes a viernes: 07:00 - 16:00
     * - Sábados: 08:00 - 12:00
     * - Domingos: No permitido
     */
    public static boolean esHorarioPermitido(LocalDateTime fechaHora) {
        DayOfWeek dia = fechaHora.getDayOfWeek();
        LocalTime hora = fechaHora.toLocalTime();

        if (dia == DayOfWeek.SATURDAY) {
            return !hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(12, 0));
        } else if (dia.getValue() >= DayOfWeek.MONDAY.getValue() && dia.getValue() <= DayOfWeek.FRIDAY.getValue()) {
            return !hora.isBefore(LocalTime.of(7, 0)) && !hora.isAfter(LocalTime.of(16, 0));
        }

        // Domingo o día inválido
        return false;
    }
}
