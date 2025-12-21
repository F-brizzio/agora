package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.GuiaConsumoDto;
import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salidas")
@CrossOrigin(origins = "*") // Permite la conexión desde tu frontend en React
public class SalidaController {

    private final SalidaService salidaService;

    public SalidaController(SalidaService salidaService) {
        this.salidaService = salidaService;
    }

    /**
     * 1. REGISTRAR GUÍA (POST)
     * Procesa la salida de productos aplicando FIFO.
     */
    @PostMapping
    public ResponseEntity<String> registrarSalida(@Valid @RequestBody GuiaConsumoDto guiaDto) {
        // Log informativo para depuración en consola
        System.out.println("📢 Recibiendo guía para área origen: " + guiaDto.getAreaOrigenId()); 
        salidaService.procesarGuiaConsumo(guiaDto);
        return ResponseEntity.ok("Guía de consumo registrada con éxito");
    }

    /**
     * 2. LISTAR HISTORIAL (GET)
     * Retorna el resumen agrupado de todas las guías emitidas.
     */
    @GetMapping
    public ResponseEntity<List<ResumenSalidaDto>> obtenerHistorial() {
        return ResponseEntity.ok(salidaService.obtenerResumenHistorial());
    }

    /**
     * 3. BUSCADOR DINÁMICO (Modificado para Modo General)
     * Si areaId no se envía, se activa la búsqueda global en todas las áreas.
     */
    @GetMapping("/buscar-productos")
    public ResponseEntity<?> buscarProductosPorArea(
            @RequestParam(required = false) Long areaId, // Se cambia a OPCIONAL para el Modo General
            @RequestParam String query) {
        // Llama al nuevo método del servicio que maneja lógica global o por área
        return ResponseEntity.ok(salidaService.buscarStockParaGuia(areaId, query));
    }
}