package com.tuempresa.bodega.movement.salida.dto;

import java.time.LocalDate;

public class ResumenSalidaDto {
    private String folio;
    private LocalDate fecha;
    private String areaOrigen;
    private Long cantidadItems; // Cuántos productos distintos se llevaron
    private Double totalUnidades; // Suma de cantidades

    public ResumenSalidaDto(String folio, LocalDate fecha, String areaOrigen, Long cantidadItems, Double totalUnidades) {
        this.folio = folio;
        this.fecha = fecha;
        this.areaOrigen = areaOrigen;
        this.cantidadItems = cantidadItems;
        this.totalUnidades = totalUnidades;
    }

    // Getters
    public String getFolio() { return folio; }
    public LocalDate getFecha() { return fecha; }
    public String getAreaOrigen() { return areaOrigen; }
    public Long getCantidadItems() { return cantidadItems; }
    public Double getTotalUnidades() { return totalUnidades; }
}