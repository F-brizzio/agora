package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalidaHistorialRepository extends JpaRepository<SalidaHistorial, Long> {

    /**
     * Obtiene el resumen agrupado por folio.
     * Se actualiza para incluir responsable y destino en la vista principal.
     * Se utiliza MAX() para campos de texto porque al agrupar por folio, 
     * se asume que todos los items de la misma guía comparten estos datos.
     */
    @Query("SELECT new com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto(" +
        "h.folio, MAX(h.fecha), MAX(h.usuarioResponsable), MAX(h.areaDestino), " +
        "MAX(h.areaOrigen), COUNT(h), SUM(h.cantidad), SUM(h.valorNeto)) " +
        "FROM SalidaHistorial h GROUP BY h.folio ORDER BY MAX(h.fecha) DESC")
    List<ResumenSalidaDto> findAllResumen();

    /**
     * Obtiene todos los productos de una guía específica para la vista de detalle.
     * Devuelve la entidad completa con campos como productName, tipoSalida y valorNeto.
     */
    List<SalidaHistorial> findByFolio(String folio);
}