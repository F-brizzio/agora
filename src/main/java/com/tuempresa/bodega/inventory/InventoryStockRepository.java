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

    // 1. Búsquedas Estándar
    List<InventoryStock> findByAreaDeTrabajo(AreaDeTrabajo areaDeTrabajo);
    
    List<InventoryStock> findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(AreaDeTrabajo area, Product product);

    // 2. Búsqueda por Producto
    List<InventoryStock> findByProduct(Product product);

    // --- 5. BÚSQUEDA POR SKU (NECESARIA PARA EDICIÓN DE INGRESOS) ---
    // Esta permite al Service encontrar el stock físico para ajustar cantidades
    @Query("SELECT s FROM InventoryStock s WHERE s.product.sku = :sku")
    List<InventoryStock> findByProductSku(@Param("sku") String sku);

    // 3. INVENTARIO COMPLETO
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, " +
           "SUM(s.cantidad), s.product.unitOfMeasure) " +
           "FROM InventoryStock s " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerInventarioCompleto();

    // 4. STOCK POR ÁREA
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, " +
           "SUM(s.cantidad), s.product.unitOfMeasure) " +
           "FROM InventoryStock s " +
           "WHERE s.areaDeTrabajo.id = :areaId " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerStockPorArea(@Param("areaId") Long areaId);
}