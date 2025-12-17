package com.tuempresa.bodega.alert;

import com.tuempresa.bodega.alert.dto.AlertaDto;
import com.tuempresa.bodega.inventory.InventoryStock;
import com.tuempresa.bodega.inventory.InventoryStockRepository;
import com.tuempresa.bodega.product.Product;
import com.tuempresa.bodega.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlertaService {

    private final ProductRepository productRepository;
    private final InventoryStockRepository inventoryRepository;

    public AlertaService(ProductRepository productRepository, InventoryStockRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<AlertaDto> generarAlertas() {
        List<AlertaDto> alertas = new ArrayList<>();
        List<Product> productos = productRepository.findAll();
        List<InventoryStock> todoElStock = inventoryRepository.findAll();

        for (Product p : productos) {
            // 1. Filtrar el stock de ESTE producto (en todas las áreas)
            List<InventoryStock> stockProducto = todoElStock.stream()
                    .filter(s -> s.getProduct().getId().equals(p.getId()))
                    .toList();

            // Calcular Cantidad Total Global
            double totalActual = stockProducto.stream().mapToDouble(InventoryStock::getCantidad).sum();

            // REGLA A: BAJO STOCK (Crítico)
            if (totalActual <= p.getMinStock()) {
                alertas.add(new AlertaDto(
                    p.getSku(), p.getName(), "CRITICO",
                    "Stock Bajo: Hay " + totalActual + " (Mínimo requerido: " + p.getMinStock() + ")"
                ));
            }

            // REGLA B: SOBRE STOCK (Advertencia)
            if (totalActual >= p.getMaxStock()) {
                alertas.add(new AlertaDto(
                    p.getSku(), p.getName(), "ADVERTENCIA",
                    "Sobre Stock: Hay " + totalActual + " (Máximo permitido: " + p.getMaxStock() + ")"
                ));
            }

            // REGLA C: ANTIGÜEDAD / VENCIMIENTO (Crítico)
            // Revisamos lote por lote si alguno lleva demasiado tiempo
            for (InventoryStock lote : stockProducto) {
                long diasEnBodega = ChronoUnit.DAYS.between(lote.getFechaIngreso(), LocalDate.now());
                
                if (diasEnBodega > p.getMaxStorageDays()) {
                    alertas.add(new AlertaDto(
                        p.getSku(), p.getName(), "CRITICO",
                        "Lote Vencido en " + lote.getAreaDeTrabajo().getNombre() + ": Lleva " + diasEnBodega + " días (Máx: " + p.getMaxStorageDays() + ")"
                    ));
                }
            }
        }

        return alertas;
    }
}