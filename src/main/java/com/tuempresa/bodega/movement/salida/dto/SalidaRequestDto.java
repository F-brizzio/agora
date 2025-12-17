package com.tuempresa.bodega.movement.salida.dto;

public class SalidaRequestDto {
    private String productSku;
    private Long areaId; // De qué área estamos sacando (ej: Cocina)
    private Double cantidad; // Cuánto sacamos

    // Getters y Setters
    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }
}