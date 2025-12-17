package com.tuempresa.bodega.report.dto;

public class ProductoInfoFilterDto {
    private String sku;
    private String nombre;
    private String categoria;
    private String proveedor;
    private String area; // Área por defecto (si aplica)

    public ProductoInfoFilterDto(String sku, String nombre, String categoria, String proveedor, String area) {
        this.sku = sku;
        this.nombre = nombre;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.area = area;
    }

    // Getters
    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getProveedor() { return proveedor; }
    public String getArea() { return area; }
}