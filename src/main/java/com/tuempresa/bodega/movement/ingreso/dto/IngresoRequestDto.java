package com.tuempresa.bodega.movement.ingreso.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class IngresoRequestDto {

    // --- ENCABEZADO (Datos de la Factura y Proveedor) ---
    private LocalDate fecha;
    private String numeroDocumento;
    private String supplierRut;
    private String supplierName;
    private String responsable; // Usuario que hace la acción

    // --- DETALLE (Lista de Productos) ---
    private List<IngresoItemDto> items;

    @Data
    public static class IngresoItemDto {
        private String productSku;
        private String productName;
        private Long areaId;       // Destino
        private Double cantidad;
        private Double costoUnitario; // Precio unitario neto

        // --- CAMPOS OPCIONALES (Solo requeridos si el producto es NUEVO) ---
        private String category;
        private String unitOfMeasure;
        private Double minStock;
        private Double maxStock;
        private Integer maxStorageDays;
    }
}