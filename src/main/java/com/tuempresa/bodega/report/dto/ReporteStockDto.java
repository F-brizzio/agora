package com.tuempresa.bodega.report.dto;

public class ReporteStockDto {
    private String concepto;        
    private Double stockActual;     
    private Long cantidadLotes;     
    private Long itemsDistintos;
    private Double valorTotal; // <--- NUEVO: Valor monetario del stock

    public ReporteStockDto(String concepto, Double stockActual, Long cantidadLotes, Long itemsDistintos, Double valorTotal) {
        this.concepto = concepto;
        this.stockActual = stockActual;
        this.cantidadLotes = cantidadLotes;
        this.itemsDistintos = itemsDistintos;
        this.valorTotal = valorTotal;
    }

    // Getters
    public String getConcepto() { return concepto; }
    public Double getStockActual() { return stockActual; }
    public Long getCantidadLotes() { return cantidadLotes; }
    public Long getItemsDistintos() { return itemsDistintos; }
    public Double getValorTotal() { return valorTotal; } // <--- Getter nuevo
}