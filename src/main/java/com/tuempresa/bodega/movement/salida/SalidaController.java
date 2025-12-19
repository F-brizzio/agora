package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.GuiaConsumoDto;
import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salidas")
@CrossOrigin(origins = "*") // Importante para que React pueda conectar sin bloqueos
public class SalidaController {

    private final SalidaService salidaService;

    public SalidaController(SalidaService salidaService) {
        this.salidaService = salidaService;
    }

    // 1. REGISTRAR GUÍA (POST)
    // Agregamos @Valid para que use las reglas que pusimos en el DTO
    @PostMapping
    public ResponseEntity<String> registrarSalida(@Valid @RequestBody GuiaConsumoDto guiaDto) {
        System.out.println("📢 Recibiendo guía para área: " + guiaDto.getAreaOrigenId()); 
        salidaService.procesarGuiaConsumo(guiaDto);
        return ResponseEntity.ok("Guía de consumo registrada con éxito");
    }

    // 2. LISTAR HISTORIAL (GET)
    // Nota: Si el error persiste, verifica que no tengas @RequestMapping("/api/salidas") 
    // repetido en HistorialSalidaController.java
    @GetMapping
    public ResponseEntity<List<ResumenSalidaDto>> obtenerHistorial() {
        return ResponseEntity.ok(salidaService.obtenerResumenHistorial());
    }

    // 3. BUSCADOR DINÁMICO (Requerimiento 3)
    // Este endpoint servirá para que el frontend filtre productos según el área origen
    @GetMapping("/buscar-productos")
    public ResponseEntity<?> buscarProductosPorArea(
            @RequestParam Long areaId, 
            @RequestParam String query) {
        return ResponseEntity.ok(salidaService.buscarStockPorAreaYNombre(areaId, query));
    }
}