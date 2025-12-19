package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial/salidas")
@CrossOrigin(origins = "*") // Para permitir conexión desde React
public class HistorialSalidaController {

    private final SalidaHistorialRepository historialRepository;

    public HistorialSalidaController(SalidaHistorialRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    // 1. OBTENER RESUMEN (Lista de guías con sus totales)
    @GetMapping
    public ResponseEntity<List<ResumenSalidaDto>> obtenerResumen() {
        // Usamos el nombre del método que definimos en el repositorio corregido
        return ResponseEntity.ok(historialRepository.findAllResumen());
    }

    // 2. OBTENER DETALLE (Lista de productos de una guía específica)
    @GetMapping("/{folio}")
    public ResponseEntity<List<SalidaHistorial>> obtenerDetalle(@PathVariable String folio) {
        return ResponseEntity.ok(historialRepository.findByFolio(folio));
    }
}