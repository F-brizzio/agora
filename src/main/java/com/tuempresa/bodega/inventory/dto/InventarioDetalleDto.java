package com.tuempresa.bodega.inventory.dto;

public class InventarioDetalleDto {
    private String productSku;
    private String productName;
    private Long areaId;
    private String areaNombre;
    private Double cantidadTotal;
    private String unidadMedida;
    private String category;

    // Constructor completo para el Query de JPQL
    public InventarioDetalleDto(String productSku, String productName, Long areaId, 
                                String areaNombre, Double cantidadTotal, 
                                String unidadMedida, String category) {
        this.productSku = productSku;
        this.productName = productName;
        this.areaId = areaId;
        this.areaNombre = areaNombre;
        this.cantidadTotal = cantidadTotal;
        this.unidadMedida = unidadMedida;
        this.category = category;
    }

    // Getters y Setters... (Generar todos)
    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public Long getAreaId() { return areaId; }
    public String getAreaNombre() { return areaNombre; }
    public Double getCantidadTotal() { return cantidadTotal; }
    public String getUnidadMedida() { return unidadMedida; }
    public String getCategory() { return category; }
}