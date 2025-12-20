package com.tuempresa.bodega.inventory.dto;

public class InventarioDetalleDto {
    private String productSku;
    private String productName;
    private String category;
    private Long areaId;        // Necesario para la función ajustarStock en el frontend
    private String areaNombre;
    private Double cantidadTotal;
    private String unidadMedida;
    private Double valorTotal;  // Valor monetario acumulado del stock

    // El orden de este constructor DEBE ser idéntico al SELECT NEW del repositorio
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

    // Getters (necesarios para que Spring los convierta a JSON)
    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public Long getAreaId() { return areaId; }
    public String getAreaNombre() { return areaNombre; }
    public Double getCantidadTotal() { return cantidadTotal; }
    public String getUnidadMedida() { return unidadMedida; }
    public Double getValorTotal() { return valorTotal; }
}