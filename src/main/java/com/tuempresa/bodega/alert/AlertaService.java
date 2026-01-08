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
            // Filtrar el stock de este producto
            List<InventoryStock> stockProducto = todoElStock.stream()
                    .filter(s -> s.getProduct() != null && s.getProduct().getId().equals(p.getId()))
                    .toList();

            double totalActual = stockProducto.stream().mapToDouble(InventoryStock::getCantidad).sum();

            // --- REGLA A: BAJO STOCK ---
            // Solo evaluar si minStock NO es null
            if (p.getMinStock() != null && totalActual <= p.getMinStock()) {
                alertas.add(new AlertaDto(
                    p.getSku(), p.getName(), "CRITICO",
                    "Stock Bajo: Hay " + totalActual + " (Mínimo requerido: " + p.getMinStock() + ")"
                ));
            }

            // --- REGLA B: SOBRE STOCK ---
            // Solo evaluar si maxStock NO es null
            if (p.getMaxStock() != null && totalActual >= p.getMaxStock()) {
                alertas.add(new AlertaDto(
                    p.getSku(), p.getName(), "ADVERTENCIA",
                    "Sobre Stock: Hay " + totalActual + " (Máximo permitido: " + p.getMaxStock() + ")"
                ));
            }

            // --- REGLA C: ANTIGÜEDAD / VENCIMIENTO ---
            // Solo evaluar si el producto tiene definido un tiempo máximo de almacenamiento
            if (p.getMaxStorageDays() != null) {
                for (InventoryStock lote : stockProducto) {
                    // Validar que el lote tenga fecha de ingreso para evitar error en ChronoUnit
                    if (lote.getFechaIngreso() != null) {
                        long diasEnBodega = ChronoUnit.DAYS.between(lote.getFechaIngreso(), LocalDate.now());
                        
                        if (diasEnBodega > p.getMaxStorageDays()) {
                            String nombreArea = (lote.getAreaDeTrabajo() != null) 
                                                ? lote.getAreaDeTrabajo().getNombre() 
                                                : "Área desconocida";
                                                
                            alertas.add(new AlertaDto(
                                p.getSku(), p.getName(), "CRITICO",
                                "Lote Vencido en " + nombreArea + ": Lleva " + diasEnBodega + " días (Máx: " + p.getMaxStorageDays() + ")"
                            ));
                        }
                    }
                }
            }
        }

        return alertas;
    }
}