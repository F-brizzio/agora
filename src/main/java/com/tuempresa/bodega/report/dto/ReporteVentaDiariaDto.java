package com.tuempresa.bodega.report.dto;

import java.time.LocalDate;

public class ReporteVentaDiariaDto {
    private LocalDate fecha;
    private Double cantidadTotal; // Número de transacciones (notas)
    private Double valorTotal;    // Dinero recaudado

    public ReporteVentaDiariaDto(LocalDate fecha, Double cantidadTotal, Double valorTotal) {
        this.fecha = fecha;
        this.cantidadTotal = cantidadTotal;
        this.valorTotal = valorTotal;
    }

    // Getters y Setters
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Double getCantidadTotal() { return cantidadTotal; }
    public void setCantidadTotal(Double cantidadTotal) { this.cantidadTotal = cantidadTotal; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
}