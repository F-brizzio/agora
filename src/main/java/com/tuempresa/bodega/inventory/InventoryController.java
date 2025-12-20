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

    @GetMapping("/completo")
    public ResponseEntity<?> getInventarioCompleto() {
        return ResponseEntity.ok(inventoryService.getInventarioCompleto());
    }

    @GetMapping("/area/{areaId}/buscar")
    public ResponseEntity<?> buscarEnStock(@PathVariable Long areaId, @RequestParam(name = "q") String query) {
        return ResponseEntity.ok(inventoryService.buscarProductosEnStock(areaId, query));
    }

    @PostMapping("/ajuste")
    public ResponseEntity<?> ajustarStock(@RequestBody AjusteStockDto ajusteDto) {
        try {
            inventoryService.procesarAjusteManual(ajusteDto);
            return ResponseEntity.ok(Map.of("message", "Ajuste guardado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}