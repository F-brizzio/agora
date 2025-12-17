package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.product.Product;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_stock")
public class InventoryStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Producto (Muchos stocks pueden ser del mismo producto)
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Relación con Área (Muchos stocks pueden estar en la misma área)
    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private AreaDeTrabajo areaDeTrabajo;

    @Column(nullable = false)
    private Double cantidad; // Usamos Double para soportar Kilos/Litros (ej: 1.5 Kg)

    @Column(nullable = false)
    private LocalDate fechaIngreso; // VITAL PARA FIFO (Lo más viejo sale primero)

    // Constructor Vacío
    public InventoryStock() {
    }

    // Constructor Completo
    public InventoryStock(Product product, AreaDeTrabajo areaDeTrabajo, Double cantidad, LocalDate fechaIngreso) {
        this.product = product;
        this.areaDeTrabajo = areaDeTrabajo;
        this.cantidad = cantidad;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public AreaDeTrabajo getAreaDeTrabajo() { return areaDeTrabajo; }
    public void setAreaDeTrabajo(AreaDeTrabajo areaDeTrabajo) { this.areaDeTrabajo = areaDeTrabajo; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}