package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.movement.salida.dto.GuiaConsumoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; // <--- ¡SIN ESTO FALLA SILENCIOSAMENTE!

@RestController
@RequestMapping("/api/salidas")
public class SalidaController {

    private final SalidaService salidaService;

    public SalidaController(SalidaService salidaService) {
        this.salidaService = salidaService;
    }

    // 1. CREAR (POST)
    @PostMapping
    public ResponseEntity<String> registrarSalida(@RequestBody GuiaConsumoDto guiaDto) {
        // --- AGREGA ESTA LÍNEA ---
        System.out.println("📢 ¡LLEGÓ LA PETICIÓN AL CONTROLADOR!"); 
        // -------------------------
        
        salidaService.procesarGuiaConsumo(guiaDto);
        return ResponseEntity.ok("Salida registrada con éxito");
    }
    // 2. LISTAR (GET) -> ¡ESTE ES EL QUE NO ENCUENTRA!
    @GetMapping
    public ResponseEntity<List<SalidaHistorial>> obtenerHistorial() {
        return ResponseEntity.ok(salidaService.obtenerHistorialCompleto());
    }
}