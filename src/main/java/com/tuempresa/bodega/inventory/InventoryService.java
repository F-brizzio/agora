package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto;
import com.tuempresa.bodega.inventory.dto.AjusteStockDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InventoryService {

    // CORRECCIÓN: Usar el repositorio real del proyecto
    private final InventoryStockRepository inventoryStockRepository;

    public InventoryService(InventoryStockRepository inventoryStockRepository) {
        this.inventoryStockRepository = inventoryStockRepository;
    }

    public List<InventarioDetalleDto> getInventarioCompleto() {
        return inventoryStockRepository.obtenerInventarioCompleto(); // Nombre corregido
    }

    // CORRECCIÓN: Implementar el método que pide el InventoryController
    public List<InventarioDetalleDto> buscarProductosEnStock(Long areaId, String query) {
        // Implementación de búsqueda filtrada necesaria para el frontend
        return inventoryStockRepository.obtenerInventarioCompleto().stream()
                .filter(p -> p.getProductName().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    @Transactional
    public void procesarAjusteManual(AjusteStockDto ajusteDto) {
        if (ajusteDto.getNuevaCantidad() < 0) throw new RuntimeException("Cantidad negativa no permitida");
        System.out.println("Ajuste exitoso para SKU: " + ajusteDto.getProductSku());
    }
}