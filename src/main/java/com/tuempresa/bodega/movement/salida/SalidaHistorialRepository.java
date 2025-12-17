package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalidaHistorialRepository extends JpaRepository<SalidaHistorial, Long> {

    // RESUMEN AGRUPADO POR FOLIO
    @Query("SELECT new com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto(" +
           "h.folio, MAX(h.fecha), MAX(h.areaOrigen), COUNT(h), SUM(h.cantidad)) " +
           "FROM SalidaHistorial h " +
           "GROUP BY h.folio")
    List<ResumenSalidaDto> obtenerResumenAgrupado();

    // DETALLE DE UN FOLIO
    List<SalidaHistorial> findByFolio(String folio);
}