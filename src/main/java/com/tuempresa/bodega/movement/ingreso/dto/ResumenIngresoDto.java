package com.tuempresa.bodega.movement.ingreso.dto;

import java.time.LocalDate;

public class ResumenIngresoDto {
    private String numeroDocumento;
    private LocalDate fecha;
    private String supplierName; // Proveedor
    private Long cantidadItems;  // Cuántos productos distintos vinieron
    private Double totalBrutoAcumulado; // Suma de toda la factura

    public ResumenIngresoDto(String numeroDocumento, LocalDate fecha, String supplierName, Long cantidadItems, Double totalBrutoAcumulado) {
        this.numeroDocumento = numeroDocumento;
        this.fecha = fecha;
        this.supplierName = supplierName;
        this.cantidadItems = cantidadItems;
        this.totalBrutoAcumulado = totalBrutoAcumulado;
    }

    // Getters
    public String getNumeroDocumento() { return numeroDocumento; }
    public LocalDate getFecha() { return fecha; }
    public String getSupplierName() { return supplierName; }
    public Long getCantidadItems() { return cantidadItems; }
    public Double getTotalBrutoAcumulado() { return totalBrutoAcumulado; }
}