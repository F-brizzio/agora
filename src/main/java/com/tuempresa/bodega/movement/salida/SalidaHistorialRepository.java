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
     * Se agrega SUM(h.valorNeto) para cumplir con el requisito de mostrar el valor total de la guía.
     * Se ordena por fecha descendente para ver lo más reciente primero.
     */
    @Query("SELECT new com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto(" +
           "h.folio, MAX(h.fecha), MAX(h.areaOrigen), COUNT(h), SUM(h.cantidad), SUM(h.valorNeto)) " +
           "FROM SalidaHistorial h " +
           "GROUP BY h.folio " +
           "ORDER BY MAX(h.fecha) DESC")
    List<ResumenSalidaDto> findAllResumen();

    // Obtiene todos los productos de una guía específica
    List<SalidaHistorial> findByFolio(String folio);
}