package com.tuempresa.bodega.movement.salida.dto;

import java.time.LocalDate;

/**
 * DTO para el resumen del historial de salidas.
 * Coincide con la consulta agrupada en SalidaHistorialRepository.
 */
public class ResumenSalidaDto {
    private String folio;
    private LocalDate fecha;
    private String responsable; 
    private String destino;     
    private String areaOrigen;
    private Long cantidadItems;
    private Double totalUnidades;
    private Double totalNeto;

    // Constructor vacío (Buena práctica para frameworks de mapeo)
    public ResumenSalidaDto() {}

    /**
     * Constructor completo. 
     * EL ORDEN ES CRÍTICO: Debe coincidir exactamente con el SELECT del Repositorio.
     */
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

    // --- GETTERS (Esenciales para que Spring genere el JSON que lee React) ---

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getAreaOrigen() { return areaOrigen; }
    public void setAreaOrigen(String areaOrigen) { this.areaOrigen = areaOrigen; }

    public Long getCantidadItems() { return cantidadItems; }
    public void setCantidadItems(Long cantidadItems) { this.cantidadItems = cantidadItems; }

    public Double getTotalUnidades() { return totalUnidades; }
    public void setTotalUnidades(Double totalUnidades) { this.totalUnidades = totalUnidades; }

    public Double getTotalNeto() { return totalNeto; }
    public void setTotalNeto(Double totalNeto) { this.totalNeto = totalNeto; }
}