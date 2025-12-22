package com.tuempresa.bodega.movement.salida.dto;

import java.time.LocalDate;

public class ResumenSalidaDto {
    private String folio;
    private LocalDate fecha;
    private String responsable; // Nuevo: Para el requisito de vista principal
    private String destino;     // Nuevo: Para el requisito de vista principal
    private String areaOrigen;
    private Long cantidadItems;
    private Double totalUnidades;
    private Double totalNeto;   // Requisito de valorización

    // Constructor actualizado para coincidir con la consulta del repositorio
    public ResumenSalidaDto(String folio, LocalDate fecha, String responsable, String destino, 
                            String areaOrigen, Long cantidadItems, Double totalUnidades, Double totalNeto) {
        this.folio = folio;
        this.fecha = fecha;
        this.responsable = responsable;
        this.destino = destino;
        this.areaOrigen = areaOrigen;
        this.cantidadItems = cantidadItems;
        this.totalUnidades = totalUnidades;
        this.totalNeto = totalNeto;
    }

    // Getters necesarios para la serialización a JSON
    public String getFolio() { return folio; }
    public LocalDate getFecha() { return fecha; }
    public String getResponsable() { return responsable; }
    public String getDestino() { return destino; }
    public String getAreaOrigen() { return areaOrigen; }
    public Long getCantidadItems() { return cantidadItems; }
    public Double getTotalUnidades() { return totalUnidades; }
    public Double getTotalNeto() { return totalNeto; }
}