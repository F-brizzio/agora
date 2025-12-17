package com.tuempresa.bodega.inventory.dto;

public class StockDisponibleDto {
    private String sku;
    private String nombreProducto;
    private String categoria;
    private Double cantidadTotal;

    public StockDisponibleDto(String sku, String nombreProducto, String categoria, Double cantidadTotal) {
        this.sku = sku;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.cantidadTotal = cantidadTotal;
    }

    // Getters
    public String getSku() { return sku; }
    public String getNombreProducto() { return nombreProducto; }
    public String getCategoria() { return categoria; }
    public Double getCantidadTotal() { return cantidadTotal; }
}