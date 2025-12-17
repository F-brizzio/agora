package com.tuempresa.bodega.area;

import jakarta.persistence.*;

@Entity
@Table(name = "areas_trabajo")
public class AreaDeTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // Ej: "Casino", "Barra"

    // Constructor Vacío
    public AreaDeTrabajo() {
    }

    // Constructor con datos
    public AreaDeTrabajo(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}