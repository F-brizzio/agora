package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory") // 👈 OJO: Confirma que tu ruta base sea esta
@CrossOrigin("*") // Permite conexión desde React
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Endpoint existente (seguramente ya tienes este)
    @GetMapping("/area/{areaId}")
    public List<InventarioDetalleDto> getStockByArea(@PathVariable Long areaId) {
        return inventoryService.getStockByArea(areaId);
    }

    // 🔥 AGREGA ESTE NUEVO ENDPOINT PARA LA OPCIÓN "GENERAL"
    @GetMapping("/all")
    public List<InventarioDetalleDto> getAllStock() {
        return inventoryService.getInventarioCompleto();
    }
}