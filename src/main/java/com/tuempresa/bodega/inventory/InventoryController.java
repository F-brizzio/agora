package com.tuempresa.bodega.inventory;


import com.tuempresa.bodega.inventory.dto.AjusteStockDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*") 
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // 1. Corregido el Type Mismatch usando <?>
    @GetMapping("/completo")
    public ResponseEntity<?> getInventarioCompleto() {
        try {
            return ResponseEntity.ok(inventoryService.getInventarioCompleto());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/area/{areaId}/buscar")
    public ResponseEntity<?> buscarEnStock(
            @PathVariable Long areaId, 
            @RequestParam(name = "q") String query) {
        try {
            return ResponseEntity.ok(inventoryService.buscarProductosEnStock(areaId, query));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<?> getStockByArea(@PathVariable Long areaId) {
        try {
            return ResponseEntity.ok(inventoryService.getStockByArea(areaId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 2. Corregido: Ahora el método procesarAjusteManual existe en el Service
    @PostMapping("/ajuste")
    public ResponseEntity<?> ajustarStock(@RequestBody AjusteStockDto ajusteDto) {
        try {
            inventoryService.procesarAjusteManual(ajusteDto);
            return ResponseEntity.ok(Map.of("message", "Ajuste procesado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}