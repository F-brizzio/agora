package com.tuempresa.bodega.product;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sku;

    private String name;
    private String description;
    
    private Double minStock;
    private Double maxStock;

    private String category;
    private String unitOfMeasure;
    private String supplierName;
    private String supplierRut; 
    
    private Integer maxStorageDays;
    private Double price;

    // --- ELIMINADO: private Double price; ---

    public Product() {}

    // Getters y Setters (Solo los necesarios)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getMinStock() { return minStock; }
    public void setMinStock(Double minStock) { this.minStock = minStock; }

    public Double getMaxStock() { return maxStock; }
    public void setMaxStock(Double maxStock) { this.maxStock = maxStock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getSupplierRut() { return supplierRut; }
    public void setSupplierRut(String supplierRut) { this.supplierRut = supplierRut; }

    public Integer getMaxStorageDays() { return maxStorageDays; }
    public void setMaxStorageDays(Integer maxStorageDays) { this.maxStorageDays = maxStorageDays; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
   
}