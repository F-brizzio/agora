package com.tuempresa.bodega.movement.salida.dto;

import java.time.LocalDate;

public class ResumenSalidaDto {
    private String folio;
    private LocalDate fecha;
    private String areaOrigen;
    private Long cantidadItems;   // Cantidad de productos distintos (filas)
    private Double totalUnidades; // Suma física de productos
    private Double totalNeto;     // Valor monetario acumulado (Nuevo Requisito)

    public ResumenSalidaDto(String folio, LocalDate fecha, String areaOrigen, Long cantidadItems, Double totalUnidades, Double totalNeto) {
        this.folio = folio;
        this.fecha = fecha;
        this.areaOrigen = areaOrigen;
        this.cantidadItems = cantidadItems;
        this.totalUnidades = totalUnidades;
        this.totalNeto = totalNeto;
    }

    // Getters
    public String getFolio() { return folio; }
    public LocalDate getFecha() { return fecha; }
    public String getAreaOrigen() { return areaOrigen; }
    public Long getCantidadItems() { return cantidadItems; }
    public Double getTotalUnidades() { return totalUnidades; }
    public Double getTotalNeto() { return totalNeto; }
}