package com.tuempresa.bodega.movement.salida;

import jakarta.persistence.*; // O javax.persistence si es Java viejo
import java.time.LocalDate;

@Entity
public class SalidaHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private String folio;
    
    // Nombres de campos internos
    private String productSku;    // Antes sku
    private String productName;   // Antes producto
    private String areaOrigen;    // Antes origen
    private String areaDestino;   // Antes destino
    
    private Double cantidad;
    private String usuarioResponsable;
    private String tipoSalida; 

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

    // --- GETTERS Y SETTERS QUE EL COMPILADOR ESTÁ PIDIENDO ---

    public Long getId() { return id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    // Aquí solucionamos: "The method getProductSku() is undefined"
    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    // Aquí solucionamos: "The method getProductName() is undefined"
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    // Aquí solucionamos: "The method getAreaOrigen() is undefined"
    public String getAreaOrigen() { return areaOrigen; }
    public void setAreaOrigen(String areaOrigen) { this.areaOrigen = areaOrigen; }

    // Aquí solucionamos: "The method getAreaDestino() is undefined"
    public String getAreaDestino() { return areaDestino; }
    public void setAreaDestino(String areaDestino) { this.areaDestino = areaDestino; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }

    public String getUsuarioResponsable() { return usuarioResponsable; }
    public void setUsuarioResponsable(String usuarioResponsable) { this.usuarioResponsable = usuarioResponsable; }

    public String getTipoSalida() { return tipoSalida; }
    public void setTipoSalida(String tipoSalida) { this.tipoSalida = tipoSalida; }
}