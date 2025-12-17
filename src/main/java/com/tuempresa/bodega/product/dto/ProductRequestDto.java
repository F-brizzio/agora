package com.tuempresa.bodega.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequestDto {
    
    @NotEmpty
    private String productId; // SKU
    
    @NotEmpty
    private String nombre;
    
    @NotEmpty
    private String categoria;
    
    private String unidadDeMedida;

    @NotEmpty
    private String proveedorNombre;
    
    @NotEmpty
    private String proveedorRut;
    
    @NotNull
    @PositiveOrZero
    private Double stockMinimo;
    
    @NotNull
    @PositiveOrZero
    private Double stockMaximo;

    @PositiveOrZero
    private Integer tiempoMaximoBodega;

    // ELIMINADO: private Double precio; 
    // Ya no se pide ni se guarda un precio fijo.
}