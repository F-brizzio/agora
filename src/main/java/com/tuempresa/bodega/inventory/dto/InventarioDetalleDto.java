package com.tuempresa.bodega.inventory.dto;

public class InventarioDetalleDto {
    private String productSku;
    private String productName;
    private String category;
    private Long areaId;        // Agregado para permitir ajustes desde el frontend
    private String areaNombre;
    private Double cantidadTotal;
    private String unidadMedida;
    private Double valorTotal;  // Valor monetario acumulado (FIFO)

    // El orden de los parámetros DEBE coincidir con el SELECT NEW del repositorio
    public InventarioDetalleDto(String productSku, String productName, String category, 
                                Long areaId, String areaNombre, Double cantidadTotal, 
                                String unidadMedida, Double valorTotal) {
        this.productSku = productSku;
        this.productName = productName;
        this.category = category;
        this.areaId = areaId;
        this.areaNombre = areaNombre;
        this.cantidadTotal = cantidadTotal;
        this.unidadMedida = unidadMedida;
        this.valorTotal = valorTotal;
    }

    // Getters
    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public Long getAreaId() { return areaId; }
    public String getAreaNombre() { return areaNombre; }
    public Double getCantidadTotal() { return cantidadTotal; }
    public String getUnidadMedida() { return unidadMedida; }
    public Double getValorTotal() { return valorTotal; }
}