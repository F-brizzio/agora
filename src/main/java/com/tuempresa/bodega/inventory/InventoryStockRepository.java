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

    // --- 1. MÉTODOS PARA LÓGICA DE NEGOCIO (FIFO Y VALIDACIONES) ---
    List<InventoryStock> findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(AreaDeTrabajo area, Product product);
    List<InventoryStock> findByAreaDeTrabajoAndProduct_NameContainingIgnoreCase(AreaDeTrabajo area, String name);

    @Query("SELECT s FROM InventoryStock s WHERE s.product.sku = :sku")
    List<InventoryStock> findByProductSku(@Param("sku") String sku);

    // --- 2. CONSULTAS PARA LA TABLA DE INVENTARIO (CON VALORIZACIÓN) ---
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, " +
           "s.areaDeTrabajo.id, s.areaDeTrabajo.nombre, " +
           "SUM(s.cantidad), s.product.unitOfMeasure, SUM(s.cantidad * s.precioCosto)) " +
           "FROM InventoryStock s " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.id, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerInventarioCompleto();

    // --- 3. CONSULTAS PARA GUÍA DE CONSUMO (MODO GENERAL Y ESPECÍFICO) ---

    /**
     * Búsqueda en una bodega específica. 
     * Eliminamos el GROUP BY para obtener el área específica de cada registro.
     */
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.StockDisponibleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.cantidad, s.product.unitOfMeasure, " +
           "s.areaDeTrabajo.id, s.areaDeTrabajo.nombre) " + // Se agregan estos 2 campos
           "FROM InventoryStock s " +
           "WHERE s.areaDeTrabajo.id = :areaId AND LOWER(s.product.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "AND s.cantidad > 0")
    List<StockDisponibleDto> buscarStockParaGuia(@Param("areaId") Long areaId, @Param("query") String query);

    /**
     * MODO GENERAL: Búsqueda global en toda la bodega.
     * Al no agrupar por SKU, el frontend recibirá una lista de dónde está cada producto físicamente.
     */
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.StockDisponibleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.cantidad, s.product.unitOfMeasure, " +
           "s.areaDeTrabajo.id, s.areaDeTrabajo.nombre) " + // Se agregan estos 2 campos
           "FROM InventoryStock s " +
           "WHERE LOWER(s.product.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "AND s.cantidad > 0")
    List<StockDisponibleDto> buscarStockGlobalParaGuia(@Param("query") String query);
}