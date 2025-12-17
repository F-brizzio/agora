package com.tuempresa.bodega.area;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaDeTrabajoRepository extends JpaRepository<AreaDeTrabajo, Long> {
    // Aquí podríamos poner métodos extra como findByNombre si los necesitamos
}