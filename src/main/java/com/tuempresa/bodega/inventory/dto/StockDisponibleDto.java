package com.tuempresa.bodega.inventory.dto;

public class StockDisponibleDto {
    private String sku;
    private String nombreProducto;
    private String categoria;
    private Double cantidadTotal;
    private String unidadMedida; // 👈 Agregado para claridad en el Frontend

    public StockDisponibleDto(String sku, String nombreProducto, String categoria, Double cantidadTotal, String unidadMedida) {
        this.sku = sku;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.cantidadTotal = cantidadTotal;
        this.unidadMedida = unidadMedida;
    }

    // Getters
    public String getSku() { return sku; }
    public String getNombreProducto() { return nombreProducto; }
    public String getCategoria() { return categoria; }
    public Double getCantidadTotal() { return cantidadTotal; }
    public String getUnidadMedida() { return unidadMedida; }
}