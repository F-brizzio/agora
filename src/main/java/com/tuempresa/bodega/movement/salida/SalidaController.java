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
    private final SalidaHistorialRepository historialRepository; // Añadido para el detalle

    public SalidaController(SalidaService salidaService, SalidaHistorialRepository historialRepository) {
        this.salidaService = salidaService;
        this.historialRepository = historialRepository;
    }

    /**
     * 1. REGISTRAR GUÍA (POST /api/salidas)
     */
    @PostMapping
    public ResponseEntity<String> registrarSalida(@Valid @RequestBody GuiaConsumoDto guiaDto) {
        salidaService.procesarGuiaConsumo(guiaDto);
        return ResponseEntity.ok("Guía de consumo registrada con éxito");
    }

    /**
     * 2. LISTAR RESUMEN (GET /api/salidas)
     * Primera instancia: Fecha, Responsable, Destino y Total Neto.
     */
    @GetMapping
    public ResponseEntity<List<ResumenSalidaDto>> obtenerHistorial() {
        // Llama a findAllResumen() que definimos en el repositorio
        return ResponseEntity.ok(salidaService.obtenerResumenHistorial());
    }

    /**
     * 3. OBTENER DETALLE (GET /api/salidas/{folio})
     * Segunda instancia: Desglose completo de productos de una guía.
     */
    @GetMapping("/{folio}")
    public ResponseEntity<List<SalidaHistorial>> obtenerDetallePorFolio(@PathVariable String folio) {
        // Busca todos los productos asociados a ese folio en el historial
        List<SalidaHistorial> detalles = historialRepository.findByFolio(folio);
        return ResponseEntity.ok(detalles);
    }

    /**
     * 4. BUSCADOR DINÁMICO
     */
    @GetMapping("/buscar-productos")
    public ResponseEntity<?> buscarProductosPorArea(
            @RequestParam(required = false) Long areaId, 
            @RequestParam String query) {
        return ResponseEntity.ok(salidaService.buscarStockParaGuia(areaId, query));
    }
}