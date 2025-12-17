package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto; // <--- USAMOS EL NUEVO DTO
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryStockRepository repository;
    private final InventoryService service; // Inyectamos el servicio si es necesario lógica extra

    public InventoryController(InventoryStockRepository repository, InventoryService service) {
        this.repository = repository;
        this.service = service;
    }

    // RF 2.1: Inventario Completo (Módulo Inventario)
    @GetMapping("/completo")
    public ResponseEntity<List<InventarioDetalleDto>> obtenerInventarioCompleto() {
        return ResponseEntity.ok(repository.obtenerInventarioCompleto());
    }

    // Para Guía de Consumo (Filtrado por Área)
    @GetMapping("/area/{areaId}")
    public ResponseEntity<List<InventarioDetalleDto>> getStockByArea(@PathVariable Long areaId) {
        // Llamamos al servicio que ya actualizamos
        return ResponseEntity.ok(service.getStockByArea(areaId));
    }
}