package com.tuempresa.bodega.movement.ingreso;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class IngresoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private String numeroDocumento;
    private String productSku;
    private String productName;
    private String category;
    private String supplierName;
    private String supplierRut;
    private String areaNombre; 
    private Double cantidad;
    private Double costoUnitario; // Este es el Neto Unitario

    private String usuarioResponsable; 

    public IngresoHistorial() {}

    public IngresoHistorial(LocalDate fecha, String numeroDocumento, String productSku, String productName, String category, String supplierName, String supplierRut, String areaNombre, Double cantidad, Double costoUnitario) {
        this.fecha = fecha;
        this.numeroDocumento = numeroDocumento;
        this.productSku = productSku;
        this.productName = productName;
        this.category = category;
        this.supplierName = supplierName;
        this.supplierRut = supplierRut;
        this.areaNombre = areaNombre;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
    }

    // --- GETTERS Y SETTERS BÁSICOS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getSupplierRut() { return supplierRut; }
    public void setSupplierRut(String supplierRut) { this.supplierRut = supplierRut; }
    public String getAreaNombre() { return areaNombre; }
    public void setAreaNombre(String areaNombre) { this.areaNombre = areaNombre; }
    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }
    public Double getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(Double costoUnitario) { this.costoUnitario = costoUnitario; }
    public String getUsuarioResponsable() { return usuarioResponsable; }
    public void setUsuarioResponsable(String usuarioResponsable) { this.usuarioResponsable = usuarioResponsable; }

    // --- CÁLCULOS AUTOMÁTICOS (LA MAGIA) ---
    // Estos métodos generan campos JSON automáticos: "totalNeto", "totalIva", "totalBruto"

    public Double getTotalNeto() {
        if (cantidad != null && costoUnitario != null) {
            return cantidad * costoUnitario;
        }
        return 0.0;
    }

    public Double getTotalIva() {
        return getTotalNeto() * 0.19; // Calculamos el 19%
    }

    public Double getTotalBruto() {
        return getTotalNeto() * 1.19; // Neto + IVA
    }
}