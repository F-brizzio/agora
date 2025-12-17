package com.tuempresa.bodega.report.dto;

public class ReporteChartDto {
    private String label;
    
    // INGRESOS
    private Double ingresoDinero;
    private Double ingresoCantidad; // Nuevo

    // SALIDAS
    private Double salidaDinero;
    private Double salidaCantidad;  // Nuevo

    public ReporteChartDto(String label, Double ingresoDinero, Double ingresoCantidad, Double salidaDinero, Double salidaCantidad) {
        this.label = label;
        this.ingresoDinero = ingresoDinero;
        this.ingresoCantidad = ingresoCantidad;
        this.salidaDinero = salidaDinero;
        this.salidaCantidad = salidaCantidad;
    }

    // Getters y Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    
    public Double getIngresoDinero() { return ingresoDinero; }
    public void setIngresoDinero(Double ingresoDinero) { this.ingresoDinero = ingresoDinero; }
    
    public Double getIngresoCantidad() { return ingresoCantidad; }
    public void setIngresoCantidad(Double ingresoCantidad) { this.ingresoCantidad = ingresoCantidad; }
    
    public Double getSalidaDinero() { return salidaDinero; }
    public void setSalidaDinero(Double salidaDinero) { this.salidaDinero = salidaDinero; }
    
    public Double getSalidaCantidad() { return salidaCantidad; }
    public void setSalidaCantidad(Double salidaCantidad) { this.salidaCantidad = salidaCantidad; }
}