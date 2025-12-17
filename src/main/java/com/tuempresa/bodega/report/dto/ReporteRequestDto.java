package com.tuempresa.bodega.report.dto;

import java.time.LocalDate;
import java.util.List;

public class ReporteRequestDto {
    
    private String tipoReporte;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String entidadFiltro;       // Ej: "PRODUCTO"
    private List<String> valoresFiltro; // Ej: ["Coca Cola", "Fanta"]
    private String filtroTipoSalida;
    // --- NUEVO CAMPO: EL FILTRO OCULTO ---
    private String filtroGlobalArea;    // Ej: "Evento"

    // Constructores
    public ReporteRequestDto() {}

    // Getters y Setters
// Getters y Setters
    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEntidadFiltro() { return entidadFiltro; }
    public void setEntidadFiltro(String entidadFiltro) { this.entidadFiltro = entidadFiltro; }

    public List<String> getValoresFiltro() { return valoresFiltro; }
    public void setValoresFiltro(List<String> valoresFiltro) { this.valoresFiltro = valoresFiltro; }

    public String getFiltroGlobalArea() { return filtroGlobalArea; }
    public void setFiltroGlobalArea(String filtroGlobalArea) { this.filtroGlobalArea = filtroGlobalArea; }

    // --- GETTER Y SETTER PARA EL TIPO DE SALIDA ---
    public String getFiltroTipoSalida() { return filtroTipoSalida; }
    public void setFiltroTipoSalida(String filtroTipoSalida) { this.filtroTipoSalida = filtroTipoSalida; }
}