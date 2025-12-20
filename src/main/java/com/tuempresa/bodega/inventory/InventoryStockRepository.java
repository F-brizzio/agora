package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto;
import com.tuempresa.bodega.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    // --- MÉTODOS PARA ELIMINAR ERRORES EN LOS SERVICIOS ---

    // Soluciona error en SalidaService (FIFO)
    List<InventoryStock> findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(AreaDeTrabajo area, Product product);

    // Soluciona error en SalidaService (Buscador dinámico)
    List<InventoryStock> findByAreaDeTrabajoAndProduct_NameContainingIgnoreCase(AreaDeTrabajo area, String name);

    // Soluciona error en IngresoService
    @Query("SELECT s FROM InventoryStock s WHERE s.product.sku = :sku")
    List<InventoryStock> findByProductSku(@Param("sku") String sku);

    // --- CONSULTAS PARA LA VISTA DE INVENTARIO ---

    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.id, " + // Se agregó areaDeTrabajo.id
           "s.areaDeTrabajo.nombre, SUM(s.cantidad), s.product.unitOfMeasure, SUM(s.cantidad * s.precioCosto)) " +
           "FROM InventoryStock s " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.id, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerInventarioCompleto();

    // Método que servirá para la búsqueda filtrada en el controlador
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.id, " +
           "s.areaDeTrabajo.nombre, SUM(s.cantidad), s.product.unitOfMeasure, SUM(s.cantidad * s.precioCosto)) " +
           "FROM InventoryStock s " +
           "WHERE s.areaDeTrabajo.id = :areaId AND LOWER(s.product.name) LIKE LOWER(:query) " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.id, s.areaDeTrabajo.nombre, s.product.unitOfMeasure")
    List<InventarioDetalleDto> obtenerStockPorAreaFiltrado(@Param("areaId") Long areaId, @Param("query") String query);
}