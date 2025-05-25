package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.model.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio de préstamos de equipos deportivos.
 * Proporciona métodos para la gestión y consulta de préstamos almacenados en la base de datos MongoDB.
 */
@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {

    /**
     * Obtiene una lista de préstamos asociados a un usuario específico.
     *
     * @param userId ID del usuario cuyos préstamos se desean consultar.
     * @return Lista de préstamos del usuario.
     */
    List<Loan> findByUserId(String userId);

    /**
     * Busca un préstamo por su ID.
     *
     * @param loanId ID del préstamo a consultar.
     * @return Préstamo encontrado, o `null` si no existe.
     */
    @Query("{ '_id' : ?0 }")
    Loan findLoanById(String loanId);

    /**
     * Obtiene una lista de préstamos realizados dentro de un rango de fechas específico.
     *
     * @param start Fecha de inicio del rango.
     * @param end Fecha de fin del rango.
     * @return Lista de préstamos realizados dentro del rango de fechas.
     */
    List<Loan> findByLoanDateTimeBetween(Date start, Date end);

    /**
     * Obtiene una lista de préstamos que aún no han sido devueltos.
     *
     * @return Lista de préstamos activos que no han sido marcados como devueltos.
     */
    default List<Loan> findAllByReturnedFalse(){
        List<Loan> loans = findAll();
        List<Loan> falseloans= new ArrayList<>();
        for (Loan l: loans){
            if(!l.isReturned()){
                falseloans.add(l);
            }
        }
        return falseloans;
    }

}