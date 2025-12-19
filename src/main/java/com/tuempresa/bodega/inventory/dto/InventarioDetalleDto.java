package com.tuempresa.bodega.inventory.dto;

public class InventarioDetalleDto {
    private String productSku;
    private String productName;
    private String category;
    private String areaNombre;
    private Double cantidadTotal;
    private String unidadMedida;
    // --- NUEVO CAMPO PARA VALORIZACIÓN ---
    private Double valorTotalStock; 

    public InventarioDetalleDto(String productSku, String productName, String category, 
                                String areaNombre, Double cantidadTotal, 
                                String unidadMedida, Double valorTotalStock) {
        this.productSku = productSku;
        this.productName = productName;
        this.category = category;
        this.areaNombre = areaNombre;
        this.cantidadTotal = cantidadTotal;
        this.unidadMedida = unidadMedida;
        this.valorTotalStock = valorTotalStock;
    }

    // Getters
    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public String getAreaNombre() { return areaNombre; }
    public Double getCantidadTotal() { return cantidadTotal; }
    public String getUnidadMedida() { return unidadMedida; }
    public Double getValorTotalStock() { return valorTotalStock; }
}