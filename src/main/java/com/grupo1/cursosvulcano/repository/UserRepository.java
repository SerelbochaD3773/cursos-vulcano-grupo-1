package com.grupo1.cursosvulcano.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo1.cursosvulcano.model.entity.User;
import com.grupo1.cursosvulcano.model.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca un usuario por su username
    Optional<User> findByUsername(String username);

    // Comprueba si ya existe un username
    boolean existsByUsername(String username);

    @Query("SELECT MAX(u.profile.registrationCode) FROM User u WHERE u.profile.registrationCode LIKE :prefixPattern")
    String findMaxRegistrationCodeByPrefix(@Param("prefixPattern") String prefixPattern);

    // Permite buscar un usuario por su código de registro en el perfil
    Optional<User> findByProfileRegistrationCode(String registrationCode);

}
