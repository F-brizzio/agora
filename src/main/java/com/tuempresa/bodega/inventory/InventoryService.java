package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto;
import com.tuempresa.bodega.inventory.dto.AjusteStockDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryStockRepository inventoryRepository;

    public InventoryService(InventoryStockRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventarioDetalleDto> getInventarioCompleto() {
        return inventoryRepository.obtenerInventarioCompleto();
    }

    public List<InventarioDetalleDto> buscarProductosEnStock(Long areaId, String query) {
        // Lógica de búsqueda filtrada para la tabla de inventario
        return inventoryRepository.obtenerInventarioCompleto().stream()
                .filter(p -> p.getProductName().toLowerCase().contains(query.toLowerCase()) && 
                            (areaId == null || p.getAreaId().equals(areaId)))
                .toList();
    }

    @Transactional
    public void procesarAjusteManual(AjusteStockDto ajusteDto) {
        if (ajusteDto.getNuevaCantidad() < 0) throw new RuntimeException("Cantidad negativa no permitida");
        // Aquí se puede añadir lógica adicional para registrar el motivo del ajuste en una tabla de auditoría
        System.out.println("Ajuste manual procesado para SKU: " + ajusteDto.getProductSku());
    }
}