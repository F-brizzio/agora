package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto; // Asegúrate de importar el DTO
import com.tuempresa.bodega.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    // 1. Búsquedas Estándar (Para lógica FIFO y validaciones)
    List<InventoryStock> findByAreaDeTrabajo(AreaDeTrabajo areaDeTrabajo);
    
    List<InventoryStock> findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(AreaDeTrabajo area, Product product);

    // 2. Búsqueda por Producto (Para el Reporte de Productos con Área)
    List<InventoryStock> findByProduct(Product product);

    // 3. INVENTARIO COMPLETO (Para el Módulo de Inventario General)
    // Agrupa todo el stock de todas las áreas
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, " +
           "SUM(s.cantidad), s.product.unitOfMeasure) " +
           "FROM InventoryStock s " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerInventarioCompleto();

    // 4. STOCK POR ÁREA (Para el Módulo Guía de Consumo) <--- ¡ESTE ES EL QUE FALTABA!
    // Filtra solo por el área seleccionada para llenar el carrito de salida
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, " +
           "SUM(s.cantidad), s.product.unitOfMeasure) " +
           "FROM InventoryStock s " +
           "WHERE s.areaDeTrabajo.id = :areaId " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerStockPorArea(@Param("areaId") Long areaId);
}