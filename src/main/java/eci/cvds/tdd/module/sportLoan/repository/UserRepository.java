package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de usuarios del sistema de préstamos deportivos.
 * Proporciona métodos para la gestión y consulta de usuarios almacenados en la base de datos MongoDB.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>{

    /**
     * Verifica si un usuario existe en la base de datos por su ID.
     *
     * @param id ID del usuario a verificar.
     * @return `true` si el usuario existe, `false` en caso contrario.
     */
    public boolean existsById(Long id);

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario a consultar.
     * @return Usuario encontrado, o `null` si no existe.
     */
    default User findUserById(String id) {
        return findById(id).orElse(null);
    }



}