package com.tuempresa.bodega.note;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate date;
    private String category; // "General", "Merma", "Venta Diaria"
    private String responsible;
    
    @Column(length = 1000)
    private String content; // Detalle escrito

    // --- CAMPOS ESPECIALES PARA MERMA ---
    private String productSku;
    private String productName; 
    private Double quantity;    
    private Double calculatedCost; 

    // --- CAMPOS ESPECIALES PARA VENTA ---
    private Double salesAmount; // Monto total vendido

    public Note() {}

    public Note(String title, LocalDate date, String category, String responsible, String content) {
        this.title = title;
        this.date = date;
        this.category = category;
        this.responsible = responsible;
        this.content = content;
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getResponsible() { return responsible; }
    public void setResponsible(String responsible) { this.responsible = responsible; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Double getCalculatedCost() { return calculatedCost; }
    public void setCalculatedCost(Double calculatedCost) { this.calculatedCost = calculatedCost; }

    public Double getSalesAmount() { return salesAmount; }
    public void setSalesAmount(Double salesAmount) { this.salesAmount = salesAmount; }

    // --- SOLUCIÓN AL ERROR ---
    // Este método conecta lo que pide el reporte (getAmount) con lo que tienes en la BD (salesAmount)
    public Double getAmount() {
        return this.salesAmount;
    }
}