package com.tuempresa.bodega.user; // Asegúrate que esto coincida con tu paquete

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// No necesitas añadir la anotación @Repository, Spring lo hace por ti
public interface UserRepository extends JpaRepository<User, Long> {

    // --- Método mágico para el Login (RF 1.1) ---

    /**
     * Busca un usuario por su nombre de usuario (username).
     * Spring Data JPA entiende el nombre de este método y automáticamente
     * generará la consulta SQL: "SELECT * FROM users WHERE username = ?"
     *
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que puede contener al User si se encuentra.
     */
    Optional<User> findByUsername(String username);

}