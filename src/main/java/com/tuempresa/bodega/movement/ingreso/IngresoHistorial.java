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
    private String numeroDocumento; // Para agrupar por factura
    private String supplierRut;
    private String supplierName;
    private String usuarioResponsable;

    private String productSku;
    private String productName;
    private String category;
    private String areaDestino;

    private Double cantidad;
    private Double costoUnitario; // <--- ¡ESTE ES EL DATO CLAVE QUE FALTABA!

    // Constructor completo
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
}