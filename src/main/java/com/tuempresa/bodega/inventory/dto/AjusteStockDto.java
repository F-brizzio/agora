package com.tuempresa.bodega.inventory.dto;

public class AjusteStockDto {
    private String productSku;
    private Long areaId;
    private Double nuevaCantidad;
    private String motivo;

    // Constructor vacío (obligatorio para JSON)
    public AjusteStockDto() {}

    // Getters y Setters
    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public Double getNuevaCantidad() { return nuevaCantidad; }
    public void setNuevaCantidad(Double nuevaCantidad) { this.nuevaCantidad = nuevaCantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}