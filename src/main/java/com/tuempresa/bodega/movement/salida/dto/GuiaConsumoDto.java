package com.tuempresa.bodega.movement.salida.dto;

import java.time.LocalDate;
import java.util.List;

public class GuiaConsumoDto {

    private Long areaOrigenId; // Origen general (Cabecera)
    private String responsable;
    private LocalDate fecha;
    private List<DetalleSalidaDto> detalles;

    // Getters y Setters de la Cabecera
    public Long getAreaOrigenId() { return areaOrigenId; }
    public void setAreaOrigenId(Long areaOrigenId) { this.areaOrigenId = areaOrigenId; }
    
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public List<DetalleSalidaDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleSalidaDto> detalles) { this.detalles = detalles; }

    // --- CLASE INTERNA DETALLE (AQUÍ FALTABA EL CAMPO) ---
    public static class DetalleSalidaDto {
        private String productSku;
        private Double cantidad;
        private Long areaDestinoId;
        private String tipoSalida; // "MERMA" o "CONSUMO"
        
        // --- ¡ESTE ES EL CAMPO QUE TE FALTABA PARA QUE FUNCIONE EL MODO GENERAL! ---
        private Long areaOrigenId; 

        // Getters y Setters del Detalle
        public String getProductSku() { return productSku; }
        public void setProductSku(String productSku) { this.productSku = productSku; }
        
        public Double getCantidad() { return cantidad; }
        public void setCantidad(Double cantidad) { this.cantidad = cantidad; }
        
        public Long getAreaDestinoId() { return areaDestinoId; }
        public void setAreaDestinoId(Long areaDestinoId) { this.areaDestinoId = areaDestinoId; }
        
        public String getTipoSalida() { return tipoSalida; }
        public void setTipoSalida(String tipoSalida) { this.tipoSalida = tipoSalida; }

        // Getter y Setter NUEVO
        public Long getAreaOrigenId() { return areaOrigenId; }
        public void setAreaOrigenId(Long areaOrigenId) { this.areaOrigenId = areaOrigenId; }
    }
}