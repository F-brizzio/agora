package com.tuempresa.bodega.movement.ingreso;

import com.tuempresa.bodega.movement.ingreso.dto.IngresoRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingresos")
public class IngresoController {

    private final IngresoService ingresoService;
    private final IngresoHistorialRepository historialRepo;

    public IngresoController(IngresoService ingresoService, IngresoHistorialRepository historialRepo) {
        this.ingresoService = ingresoService;
        this.historialRepo = historialRepo;
    }

    // --- 1. REGISTRAR INGRESO (FACTURA + DETALLES) ---
    @PostMapping
    public ResponseEntity<?> registrarIngreso(@RequestBody IngresoRequestDto request) {
        try {
            // Llamamos al método 'procesarIngreso' que actualizamos en el Service
            ingresoService.procesarIngreso(request);
            return ResponseEntity.ok("✅ Ingreso registrado exitosamente.");
        } catch (RuntimeException e) {
            // Errores de negocio (Proveedor duplicado, faltan datos, etc.)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Errores inesperados
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error interno: " + e.getMessage());
        }
    }

    // --- 2. OBTENER HISTORIAL (Para consultas) ---
    @GetMapping("/historial")
    public ResponseEntity<List<IngresoHistorial>> obtenerHistorial() {
        List<IngresoHistorial> lista = historialRepo.findAll();
        // Ordenar por ID descendente (lo más nuevo primero)
        lista.sort((a, b) -> b.getId().compareTo(a.getId())); 
        return ResponseEntity.ok(lista);
    }
}