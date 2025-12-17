package com.tuempresa.bodega.alert.dto;

public class AlertaDto {
    private String productSku;
    private String productName;
    private String tipo;    // "CRITICO" (Bajo stock o Vencido), "ADVERTENCIA" (Sobre Stock)
    private String mensaje; // "Quedan solo 5 unidades (Mín: 10)"

    public AlertaDto(String productSku, String productName, String tipo, String mensaje) {
        this.productSku = productSku;
        this.productName = productName;
        this.tipo = tipo;
        this.mensaje = mensaje;
    }

    // Getters
    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public String getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
}