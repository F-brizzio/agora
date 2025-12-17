package com.tuempresa.bodega.report.dto;

public class ReporteGastosDto {
    private String concepto;        // Nombre del Proveedor, Categoría o Producto
    private Double totalGastado;    // Dinero Total
    private Double unidadesCompradas; // Cantidad Física
    private Double costoPromedio;   // Precio promedio de compra
    private Long cantidadFacturas;  // En cuántas facturas aparece

    public ReporteGastosDto(String concepto, Double totalGastado, Double unidadesCompradas, Long cantidadFacturas) {
        this.concepto = concepto;
        this.totalGastado = totalGastado;
        this.unidadesCompradas = unidadesCompradas;
        this.cantidadFacturas = cantidadFacturas;
        
        // Cálculo seguro del promedio (evitar división por cero)
        if (unidadesCompradas != null && unidadesCompradas > 0) {
            this.costoPromedio = totalGastado / unidadesCompradas;
        } else {
            this.costoPromedio = 0.0;
        }
    }

    // Getters
    public String getConcepto() { return concepto; }
    public Double getTotalGastado() { return totalGastado; }
    public Double getUnidadesCompradas() { return unidadesCompradas; }
    public Double getCostoPromedio() { return costoPromedio; }
    public Long getCantidadFacturas() { return cantidadFacturas; }
}