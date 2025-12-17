package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto; // <--- USAMOS EL NUEVO DTO
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryStockRepository inventoryRepository;

    public InventoryService(InventoryStockRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // Método para el módulo de Guía de Consumo
    // Ahora devuelve InventarioDetalleDto (el mismo que el módulo de Inventario)
    public List<InventarioDetalleDto> getStockByArea(Long areaId) {
        return inventoryRepository.obtenerStockPorArea(areaId);
    }
    
    // Método para el módulo de Inventario General (si lo tuvieras aquí)
    public List<InventarioDetalleDto> getInventarioCompleto() {
        return inventoryRepository.obtenerInventarioCompleto();
    }
}