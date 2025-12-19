package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto;
import com.tuempresa.bodega.inventory.dto.StockDisponibleDto;
import com.tuempresa.bodega.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    // 1. BÚSQUEDAS ESTÁNDAR
    List<InventoryStock> findByAreaDeTrabajo(AreaDeTrabajo areaDeTrabajo);
    
    // Crucial para FIFO: Trae los lotes del más antiguo al más nuevo
    List<InventoryStock> findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(AreaDeTrabajo area, Product product);

    // --- SOLUCIÓN AL ERROR DE COMPILACIÓN ---
    // Este método permite filtrar productos por nombre dentro de un área específica
    List<InventoryStock> findByAreaDeTrabajoAndProduct_NameContainingIgnoreCase(AreaDeTrabajo area, String name);

    // 2. BÚSQUEDA POR SKU
    @Query("SELECT s FROM InventoryStock s WHERE s.product.sku = :sku")
    List<InventoryStock> findByProductSku(@Param("sku") String sku);

    // 3. INVENTARIO COMPLETO (Agrupado para vista de tabla)
    // He agregado SUM(s.cantidad * s.precioCosto) para que puedas ver el valor total en dinero del inventario
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, " +
           "SUM(s.cantidad), s.product.unitOfMeasure, SUM(s.cantidad * s.precioCosto)) " +
           "FROM InventoryStock s " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerInventarioCompleto();

    // 4. STOCK POR ÁREA (Para filtros rápidos en el Frontend)
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, " +
           "SUM(s.cantidad), s.product.unitOfMeasure, SUM(s.cantidad * s.precioCosto)) " +
           "FROM InventoryStock s " +
           "WHERE s.areaDeTrabajo.id = :areaId " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerStockPorArea(@Param("areaId") Long areaId);



    @Query("SELECT new com.tuempresa.bodega.inventory.dto.StockDisponibleDto(" +
       "s.product.sku, s.product.name, s.product.category, SUM(s.cantidad), s.product.unitOfMeasure) " +
       "FROM InventoryStock s " +
       "WHERE s.areaDeTrabajo.id = :areaId AND LOWER(s.product.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
       "GROUP BY s.product.sku, s.product.name, s.product.category, s.product.unitOfMeasure")
    List<StockDisponibleDto> buscarStockParaGuia(@Param("areaId") Long areaId, @Param("query") String query);
}

