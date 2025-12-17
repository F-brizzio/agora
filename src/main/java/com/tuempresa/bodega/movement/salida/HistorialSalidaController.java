package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial/salidas")
public class HistorialSalidaController {

    private final SalidaHistorialRepository historialRepository;

    public HistorialSalidaController(SalidaHistorialRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    @GetMapping
    public ResponseEntity<List<ResumenSalidaDto>> obtenerResumen() {
        return ResponseEntity.ok(historialRepository.obtenerResumenAgrupado());
    }

    @GetMapping("/{folio}")
    public ResponseEntity<List<SalidaHistorial>> obtenerDetalle(@PathVariable String folio) {
        return ResponseEntity.ok(historialRepository.findByFolio(folio));
    }
}