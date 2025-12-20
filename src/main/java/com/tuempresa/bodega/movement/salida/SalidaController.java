package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.GuiaConsumoDto;
import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/salidas")
@CrossOrigin(origins = "*")
public class SalidaController {

    private final SalidaService salidaService;

    public SalidaController(SalidaService salidaService) {
        this.salidaService = salidaService;
    }

    @PostMapping
    public ResponseEntity<String> registrarSalida(@Valid @RequestBody GuiaConsumoDto guiaDto) {
        salidaService.procesarGuiaConsumo(guiaDto);
        return ResponseEntity.ok("Guía de consumo registrada con éxito");
    }

    @GetMapping
    public ResponseEntity<List<ResumenSalidaDto>> obtenerHistorial() {
        return ResponseEntity.ok(salidaService.obtenerResumenHistorial());
    }

    @GetMapping("/buscar-productos")
    public ResponseEntity<?> buscarProductosPorArea(
            @RequestParam(required = false) Long areaId, // areaId ahora es opcional para modo General
            @RequestParam String query) {
        return ResponseEntity.ok(salidaService.buscarStockParaGuia(areaId, query));
    }
}