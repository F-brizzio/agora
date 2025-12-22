package com.tuempresa.bodega.inventory.dto;

/**
 * DTO para representar el stock disponible durante la búsqueda en la Guía de Consumo.
 * Incluye información del área para permitir el "Modo General" con origen real.
 */
public class StockDisponibleDto {
    private String sku;
    private String nombreProducto;
    private String categoria;
    private Double cantidadTotal;
    private String unidadMedida;
    private Long areaId;       // 👈 ID del área física donde está el stock
    private String areaNombre; // 👈 Nombre del área física (ej: "Bodega 1", "Casino")

    // Constructor actualizado para coincidir con la consulta JPQL del repositorio
    public StockDisponibleDto(String sku, String nombreProducto, String categoria, 
                              Double cantidadTotal, String unidadMedida, 
                              Long areaId, String areaNombre) {
        this.sku = sku;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.cantidadTotal = cantidadTotal;
        this.unidadMedida = unidadMedida;
        this.areaId = areaId;
        this.areaNombre = areaNombre;
    }

    // Getters
    public String getSku() { return sku; }
    public String getNombreProducto() { return nombreProducto; }
    public String getCategoria() { return categoria; }
    public Double getCantidadTotal() { return cantidadTotal; }
    public String getUnidadMedida() { return unidadMedida; }
    public Long getAreaId() { return areaId; }
    public String getAreaNombre() { return areaNombre; }
}