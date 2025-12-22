package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalidaHistorialRepository extends JpaRepository<SalidaHistorial, Long> {

    @Query("SELECT new com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto(" +
           "h.folio, " +                // 1. String
           "MAX(h.fecha), " +           // 2. LocalDate
           "MAX(h.usuarioResponsable), " + // 3. String (Añadido)
           "MAX(h.areaDestino), " +        // 4. String (Añadido)
           "MAX(h.areaOrigen), " +         // 5. String
           "COUNT(h), " +               // 6. Long
           "SUM(h.cantidad), " +        // 7. Double
           "SUM(h.valorNeto)) " +       // 8. Double (El valor que te faltaba)
           "FROM SalidaHistorial h " +
           "GROUP BY h.folio " +
           "ORDER BY MAX(h.fecha) DESC")
    List<ResumenSalidaDto> findAllResumen();

    List<SalidaHistorial> findByFolio(String folio);
}