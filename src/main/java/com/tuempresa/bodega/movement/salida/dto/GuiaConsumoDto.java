package com.tuempresa.bodega.movement.salida.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

public class GuiaConsumoDto {

    @NotNull(message = "El área de origen de la cabecera es obligatoria")
    private Long areaOrigenId; 

    @NotBlank(message = "Debe indicar un responsable")
    private String responsable;

    @NotNull(message = "La fecha no puede estar vacía")
    private LocalDate fecha;

    @NotEmpty(message = "La guía debe contener al menos un producto")
    private List<DetalleSalidaDto> detalles;

    // Getters y Setters
    public Long getAreaOrigenId() { return areaOrigenId; }
    public void setAreaOrigenId(Long areaOrigenId) { this.areaOrigenId = areaOrigenId; }
    
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public List<DetalleSalidaDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleSalidaDto> detalles) { this.detalles = detalles; }

    public static class DetalleSalidaDto {
        @NotBlank(message = "El SKU del producto es obligatorio")
        private String productSku;

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero")
        private Double cantidad;

        @NotBlank(message = "Debe especificar si es CONSUMO o MERMA")
        private String tipoSalida; 

        private Long areaOrigenId;  // Origen específico (Uso obligatorio en modo General)
        private Long areaDestinoId; // Destino (Uso obligatorio en modo General)

        // Getters y Setters
        public String getProductSku() { return productSku; }
        public void setProductSku(String productSku) { this.productSku = productSku; }
        
        public Double getCantidad() { return cantidad; }
        public void setCantidad(Double cantidad) { this.cantidad = cantidad; }
        
        public Long getAreaDestinoId() { return areaDestinoId; }
        public void setAreaDestinoId(Long areaDestinoId) { this.areaDestinoId = areaDestinoId; }
        
        public String getTipoSalida() { return tipoSalida; }
        public void setTipoSalida(String tipoSalida) { this.tipoSalida = tipoSalida; }

        public Long getAreaOrigenId() { return areaOrigenId; }
        public void setAreaOrigenId(Long areaOrigenId) { this.areaOrigenId = areaOrigenId; }
    }
}