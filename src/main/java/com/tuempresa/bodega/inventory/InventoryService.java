package com.tuempresa.bodega.inventory;

import com.tuempresa.bodega.inventory.dto.InventarioDetalleDto;
import com.tuempresa.bodega.inventory.dto.AjusteStockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private LoteRepository loteRepository;

    public List<InventarioDetalleDto> getInventarioCompleto() {
        return loteRepository.obtenerInventarioAgrupado();
    }

    public List<InventarioDetalleDto> buscarProductosEnStock(Long areaId, String query) {
        return loteRepository.buscarPorAreaYNombre(areaId, "%" + query + "%");
    }

    @Transactional
    public void procesarAjusteManual(AjusteStockDto ajusteDto) {
        // Por ahora, solo validación. Aquí deberías crear un registro de ajuste en la DB.
        if (ajusteDto.getNuevaCantidad() < 0) throw new RuntimeException("Cantidad negativa no permitida");
        System.out.println("Ajuste exitoso para SKU: " + ajusteDto.getProductSku());
    }
}