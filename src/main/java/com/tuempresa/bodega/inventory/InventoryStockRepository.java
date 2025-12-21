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

    // Crucial para SalidaService: Obtiene lotes para descontar por orden de llegada
    List<InventoryStock> findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(AreaDeTrabajo area, Product product);

    // Buscador dinámico por nombre dentro de una misma bodega
    List<InventoryStock> findByAreaDeTrabajoAndProduct_NameContainingIgnoreCase(AreaDeTrabajo area, String name);

    // Buscador por SKU para el servicio de Ingreso
    @Query("SELECT s FROM InventoryStock s WHERE s.product.sku = :sku")
    List<InventoryStock> findByProductSku(@Param("sku") String sku);


    // --- 2. CONSULTAS PARA LA TABLA DE INVENTARIO (CON VALORIZACIÓN) ---

    // Esta consulta usa 8 parámetros para coincidir exactamente con el DTO corregido
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, " +           // 1. String
           "s.product.name, " +          // 2. String
           "s.product.category, " +      // 3. String
           "s.areaDeTrabajo.id, " +      // 4. Long
           "s.areaDeTrabajo.nombre, " +  // 5. String
           "SUM(s.cantidad), " +         // 6. Double
           "s.product.unitOfMeasure, " + // 7. String
           "SUM(s.cantidad * s.precioCosto)) " + // 8. Double (Valor Total FIFO)
           "FROM InventoryStock s " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.id, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerInventarioCompleto();


    // --- 3. CONSULTAS PARA GUÍA DE CONSUMO (MODO GENERAL Y ESPECÍFICO) ---

    // Búsqueda en una bodega específica (Casino, Coffee, etc.)
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.StockDisponibleDto(" +
           "s.product.sku, s.product.name, s.product.category, SUM(s.cantidad), s.product.unitOfMeasure) " +
           "FROM InventoryStock s " +
           "WHERE s.areaDeTrabajo.id = :areaId AND LOWER(s.product.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.product.unitOfMeasure")
    List<StockDisponibleDto> buscarStockParaGuia(@Param("areaId") Long areaId, @Param("query") String query);

    // MODO GENERAL: Búsqueda global en toda la bodega sumando stock de todas las áreas
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.StockDisponibleDto(" +
           "s.product.sku, s.product.name, s.product.category, SUM(s.cantidad), s.product.unitOfMeasure) " +
           "FROM InventoryStock s " +
           "WHERE LOWER(s.product.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.product.unitOfMeasure")
    List<StockDisponibleDto> buscarStockGlobalParaGuia(@Param("query") String query);
}