package com.tuempresa.bodega.movement.ingreso;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class IngresoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private String numeroDocumento; 
    private String supplierRut;
    private String supplierName;
    private String usuarioResponsable;

    private String productSku;
    private String productName;
    private String category;
    
    // En la base de datos se llama 'areaDestino', pero tu código busca 'getAreaNombre'
    private String areaDestino; 

    private Double cantidad;
    private Double costoUnitario; 

    public IngresoHistorial(LocalDate fecha, 
                            String numeroDocumento, 
                            String productSku, 
                            String productName, 
                            String category,
                            String supplierName, 
                            String supplierRut,
                            String areaDestino, 
                            Double cantidad, 
                            Double costoUnitario) {
        this.fecha = fecha;
        this.numeroDocumento = numeroDocumento;
        this.productSku = productSku;
        this.productName = productName;
        this.category = category;
        this.supplierName = supplierName;
        this.supplierRut = supplierRut;
        this.areaDestino = areaDestino;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
    }

    // --- MÉTODOS CALCULADOS (SOLUCIÓN A TUS ERRORES) ---

    // 1. Soluciona el error "getAreaNombre() undefined"
    // Simplemente devuelve el valor de areaDestino
    public String getAreaNombre() {
        return this.areaDestino;
    }

    // 2. Calcula el Neto al vuelo (Cantidad * Costo)
    public Double getTotalNeto() {
        if (cantidad == null || costoUnitario == null) return 0.0;
        return cantidad * costoUnitario;
    }

    // 3. Soluciona el error "getTotalBruto() undefined"
    // Calcula el Bruto (Neto * 1.19)
    public Double getTotalBruto() {
        return getTotalNeto() * 1.19; 
    }
    
    // 4. Opcional: Para obtener el IVA si lo necesitas
    public Double getTotalIva() {
        return getTotalNeto() * 0.19;
    }
    
}