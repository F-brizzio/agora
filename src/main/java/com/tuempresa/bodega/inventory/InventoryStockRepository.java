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

    // --- MÉTODOS PARA SERVICIOS ---
    List<InventoryStock> findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(AreaDeTrabajo area, Product product);
    List<InventoryStock> findByAreaDeTrabajoAndProduct_NameContainingIgnoreCase(AreaDeTrabajo area, String name);
    
    @Query("SELECT s FROM InventoryStock s WHERE s.product.sku = :sku")
    List<InventoryStock> findByProductSku(@Param("sku") String sku);

    // --- CONSULTAS DE INVENTARIO (Sincronizadas con DTO) ---
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.InventarioDetalleDto(" +
           "s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.id, " +
           "s.areaDeTrabajo.nombre, SUM(s.cantidad), s.product.unitOfMeasure, SUM(s.cantidad * s.precioCosto)) " +
           "FROM InventoryStock s " +
           "GROUP BY s.product.sku, s.product.name, s.product.category, s.areaDeTrabajo.id, s.areaDeTrabajo.nombre, s.product.unitOfMeasure " +
           "HAVING SUM(s.cantidad) > 0")
    List<InventarioDetalleDto> obtenerInventarioCompleto();

    // --- BÚSQUEDA PARA GUÍAS ---
    // Búsqueda en área específica
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.StockDisponibleDto(" +
       "s.product.sku, s.product.name, s.product.category, SUM(s.cantidad), s.product.unitOfMeasure) " +
       "FROM InventoryStock s " +
       "WHERE s.areaDeTrabajo.id = :areaId AND LOWER(s.product.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
       "GROUP BY s.product.sku, s.product.name, s.product.category, s.product.unitOfMeasure")
    List<StockDisponibleDto> buscarStockParaGuia(@Param("areaId") Long areaId, @Param("query") String query);

    // Búsqueda Global (Modo General)
    @Query("SELECT new com.tuempresa.bodega.inventory.dto.StockDisponibleDto(" +
       "s.product.sku, s.product.name, s.product.category, SUM(s.cantidad), s.product.unitOfMeasure) " +
       "FROM InventoryStock s " +
       "WHERE LOWER(s.product.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
       "GROUP BY s.product.sku, s.product.name, s.product.category, s.product.unitOfMeasure")
    List<StockDisponibleDto> buscarStockGlobalParaGuia(@Param("query") String query);
}