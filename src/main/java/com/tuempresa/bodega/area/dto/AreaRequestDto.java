package com.tuempresa.bodega.area.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AreaRequestDto {

    @NotEmpty(message = "El nombre del área es obligatorio")
    private String nombre;
}