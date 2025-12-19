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

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private AreaDeTrabajo areaDeTrabajo;

    @Column(nullable = false)
    private Double cantidad; 

    @Column(nullable = false)
    private LocalDate fechaIngreso; // VITAL PARA FIFO

    // --- NUEVO CAMPO: VALOR DEL LOTE ---
    @Column(nullable = false)
    private Double precioCosto; 

    public InventoryStock() {
    }

    // Constructor Actualizado
    public InventoryStock(Product product, AreaDeTrabajo areaDeTrabajo, Double cantidad, LocalDate fechaIngreso, Double precioCosto) {
        this.product = product;
        this.areaDeTrabajo = areaDeTrabajo;
        this.cantidad = cantidad;
        this.fechaIngreso = fechaIngreso;
        this.precioCosto = precioCosto;
        
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

    public Double getPrecioCosto() { return precioCosto; }
    public void setPrecioCosto(Double precioCosto) { this.precioCosto = precioCosto; }
}