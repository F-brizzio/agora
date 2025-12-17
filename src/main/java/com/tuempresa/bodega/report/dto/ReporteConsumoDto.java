package com.tuempresa.bodega.report.dto;

public class ReporteConsumoDto {
    private String label; // Nombre del producto/area
    
    // Datos Totales (Suma de todo)
    private Double totalUnidades; 
    private Long cantidadGuias; 
    private Double valorEstimado;

    // --- NUEVOS CAMPOS DESGLOSADOS ---
    private Double cantConsumo;
    private Double valorConsumo;
    private Double cantMerma;
    private Double valorMerma;

    // Constructor Actualizado
    public ReporteConsumoDto(String label, Double totalUnidades, Long cantidadGuias, Double valorEstimado, 
                             Double cantConsumo, Double valorConsumo, Double cantMerma, Double valorMerma) {
        this.label = label;
        this.totalUnidades = totalUnidades;
        this.cantidadGuias = cantidadGuias;
        this.valorEstimado = valorEstimado;
        this.cantConsumo = cantConsumo;
        this.valorConsumo = valorConsumo;
        this.cantMerma = cantMerma;
        this.valorMerma = valorMerma;
    }

    // Getters y Setters
    public String getLabel() { return label; }
    public Double getTotalUnidades() { return totalUnidades; }
    public Long getCantidadGuias() { return cantidadGuias; }
    public Double getValorEstimado() { return valorEstimado; }
    
    public Double getCantConsumo() { return cantConsumo; }
    public Double getValorConsumo() { return valorConsumo; }
    public Double getCantMerma() { return cantMerma; }
    public Double getValorMerma() { return valorMerma; }
}