package com.tuempresa.bodega.movement.salida.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para capturar la Guía de Consumo desde el Frontend.
 */
public class GuiaConsumoDto {

    // SE ELIMINA @NotNull: Para permitir que sea null en el "Modo General"
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

    /**
     * Clase interna para los items de la guía.
     */
    public static class DetalleSalidaDto {
        @NotBlank(message = "El SKU del producto es obligatorio")
        private String productSku;

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero")
        private Double cantidad;

        @NotBlank(message = "Debe especificar si es CONSUMO o MERMA")
        private String tipoSalida; 

        // IMPORTANTE: Estos campos permiten que cada producto tenga su propio origen y destino
        private Long areaOrigenId;  // El ID real de la bodega donde está el producto
        private Long areaDestinoId; // El ID de la bodega donde se envía (si aplica)

        // Getters y Setters
        public String getProductSku() { return productSku; }
        public void setProductSku(String productSku) { this.productSku = productSku; }
        
        public Double getCantidad() { return cantidad; }
        public void setCantidad(Double cantidad) { this.cantidad = cantidad; }
        
        public String getTipoSalida() { return tipoSalida; }
        public void setTipoSalida(String tipoSalida) { this.tipoSalida = tipoSalida; }

        public Long getAreaOrigenId() { return areaOrigenId; }
        public void setAreaOrigenId(Long areaOrigenId) { this.areaOrigenId = areaOrigenId; }

        public Long getAreaDestinoId() { return areaDestinoId; }
        public void setAreaDestinoId(Long areaDestinoId) { this.areaDestinoId = areaDestinoId; }
    }
}