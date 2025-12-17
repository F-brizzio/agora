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

    @PostMapping
    public ResponseEntity<?> registrarIngreso(@RequestBody IngresoRequestDto request) {
        try {
            ingresoService.procesarIngreso(request);
            return ResponseEntity.ok("✅ Ingreso registrado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<List<IngresoHistorial>> obtenerHistorial() {
        List<IngresoHistorial> lista = historialRepo.findAll();
        lista.sort((a, b) -> b.getId().compareTo(a.getId())); 
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarIngreso(@PathVariable Long id, @RequestBody IngresoHistorial nuevosDatos) {
        try {
            IngresoHistorial actualizado = ingresoService.actualizarIngreso(id, nuevosDatos);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar registro.");
        }
    }
}