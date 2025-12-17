package com.tuempresa.bodega.movement.salida.dto;

import java.time.LocalDate;
import java.util.List;

public class GuiaConsumoDto {
    private LocalDate fecha;
    private Long areaOrigenId;
    private String responsable;
    private List<DetalleSalidaDto> detalles;

    // Getters y Setters de la clase principal...
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Long getAreaOrigenId() { return areaOrigenId; }
    public void setAreaOrigenId(Long areaOrigenId) { this.areaOrigenId = areaOrigenId; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public List<DetalleSalidaDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleSalidaDto> detalles) { this.detalles = detalles; }

    // --- CLASE INTERNA MODIFICADA ---
    public static class DetalleSalidaDto {
        private String productSku;
        private Double cantidad;
        private Long areaDestinoId;
        
        // NUEVO CAMPO AQUÍ
        private String tipoSalida; // "CONSUMO" o "MERMA"

        // Getters y Setters
        public String getProductSku() { return productSku; }
        public void setProductSku(String productSku) { this.productSku = productSku; }
        public Double getCantidad() { return cantidad; }
        public void setCantidad(Double cantidad) { this.cantidad = cantidad; }
        public Long getAreaDestinoId() { return areaDestinoId; }
        public void setAreaDestinoId(Long areaDestinoId) { this.areaDestinoId = areaDestinoId; }
        
        public String getTipoSalida() { return tipoSalida; }
        public void setTipoSalida(String tipoSalida) { this.tipoSalida = tipoSalida; }
    }
}