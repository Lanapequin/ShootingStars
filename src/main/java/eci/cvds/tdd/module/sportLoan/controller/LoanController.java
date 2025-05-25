package eci.cvds.tdd.module.sportLoan.controller;

import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Controlador encargado de la gestión de préstamos deportivos.
 * Proporciona endpoints para crear, devolver y consultar préstamos de equipos deportivos.
 */
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    @Autowired
    private LoanService loanService;

    /**
     * Crea un nuevo préstamo de equipo deportivo.
     * Asocia el préstamo a un usuario y envía un recordatorio de devolución.
     *
     * @param request Detalles del préstamo solicitado.
     * @return ResponseEntity con el préstamo creado o un mensaje de error.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createLoan(@RequestBody LoanRequest request,@RequestHeader("Authorization") String token) {
        try {
            Loan loan = loanService.createLoan(request);
            loanService.addLoanToUser(loan);
            loanService.sendReturnReminder(loan.getId());
            return new ResponseEntity<>(loan, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Registra la devolución de un equipo prestado.
     *
     * @param details Detalles de la devolución del equipo.
     * @return ResponseEntity con el préstamo actualizado.
     */
    @PostMapping("/return")
    public ResponseEntity<Loan> returnLoan(@RequestBody ReturnDetails details,@RequestHeader("Authorization") String token) {
        Loan loan=loanService.returnLoan(details);
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    /**
     * Consulta un préstamo por su ID.
     *
     * @param loanId ID del préstamo a consultar.
     * @return ResponseEntity con el préstamo encontrado o un estado HTTP 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable("id") String loanId,@RequestHeader("Authorization") String token) {
        Loan loan = loanService.getLoanById(loanId);
        if (loan != null) {
            return new ResponseEntity<>(loan, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Lista todos los préstamos asociados a un usuario específico.
     *
     * @param userId ID del usuario cuyos préstamos se desean consultar.
     * @return ResponseEntity con la lista de préstamos del usuario.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> listLoansByUser(@PathVariable String userId,@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(loanService.listLoansByUser(userId), HttpStatus.OK);
    }

    /**
     * Lista los préstamos dentro de un rango de fechas especificado.
     *
     * @param from Fecha de inicio del rango.
     * @param to   Fecha de fin del rango.
     * @return ResponseEntity con la lista de préstamos dentro del rango.
     */
    @GetMapping("/range")
    public ResponseEntity<List<Loan>> listLoansByDateRange(@RequestParam Date from, @RequestParam Date to,@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(loanService.listLoansByDateRange(from, to), HttpStatus.OK);
    }

     @GetMapping("/{userId}")
    public ResponseEntity<List<String>> getUserNotifications(@PathVariable String userId,@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(loanService.getNotificationsForUser(userId), HttpStatus.OK);
    }
    
}
