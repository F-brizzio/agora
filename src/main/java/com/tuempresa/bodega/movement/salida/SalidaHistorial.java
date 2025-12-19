package com.tuempresa.bodega.movement.salida;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "salida_historial")
public class SalidaHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private String folio;
    
    private String productSku; 
    private String productName; 
    private String areaOrigen;  
    private String areaDestino; 
    
    private Double cantidad;
    private String usuarioResponsable;
    private String tipoSalida; // CONSUMO o MERMA

    // --- NUEVO CAMPO PARA EL REQUISITO DE VALOR NETO ---
    private Double valorNeto;

    public SalidaHistorial() {}

    public SalidaHistorial(LocalDate fecha, String folio, String productSku, String productName, String areaOrigen, String areaDestino, Double cantidad) {
        this.fecha = fecha;
        this.folio = folio;
        this.productSku = productSku;
        this.productName = productName;
        this.areaOrigen = areaOrigen;
        this.areaDestino = areaDestino;
        this.cantidad = cantidad;
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getAreaOrigen() { return areaOrigen; }
    public void setAreaOrigen(String areaOrigen) { this.areaOrigen = areaOrigen; }

    public String getAreaDestino() { return areaDestino; }
    public void setAreaDestino(String areaDestino) { this.areaDestino = areaDestino; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }

    public String getUsuarioResponsable() { return usuarioResponsable; }
    public void setUsuarioResponsable(String usuarioResponsable) { this.usuarioResponsable = usuarioResponsable; }

    public String getTipoSalida() { return tipoSalida; }
    public void setTipoSalida(String tipoSalida) { this.tipoSalida = tipoSalida; }

    // Getter y Setter para el Valor Neto
    public Double getValorNeto() { return valorNeto; }
    public void setValorNeto(Double valorNeto) { this.valorNeto = valorNeto; }
}