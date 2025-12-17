package com.tuempresa.bodega.report;

import com.tuempresa.bodega.area.AreaDeTrabajoRepository;
import com.tuempresa.bodega.inventory.InventoryStock;
import com.tuempresa.bodega.inventory.InventoryStockRepository;
import com.tuempresa.bodega.movement.salida.SalidaHistorial;
import com.tuempresa.bodega.movement.salida.SalidaHistorialRepository;
import com.tuempresa.bodega.product.ProductRepository;
import com.tuempresa.bodega.report.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;
    private final ProductRepository productRepo;
    private final AreaDeTrabajoRepository areaRepo;
    private final InventoryStockRepository stockRepo;
    private final SalidaHistorialRepository salidaRepo;

    public ReporteController(ReporteService reporteService, ProductRepository productRepo, AreaDeTrabajoRepository areaRepo, InventoryStockRepository stockRepo, SalidaHistorialRepository salidaRepo) {
        this.reporteService = reporteService;
        this.productRepo = productRepo;
        this.areaRepo = areaRepo;
        this.stockRepo = stockRepo;
        this.salidaRepo = salidaRepo;
    }

    // --- REPORTES ---
    @PostMapping("/gastos")
    public ResponseEntity<List<ReporteGastosDto>> generarReporteGastos(@RequestBody ReporteRequestDto request) {
        return ResponseEntity.ok(reporteService.generarReporteGastos(request));
    }

    @PostMapping("/consumo")
    public ResponseEntity<List<ReporteConsumoDto>> generarReporteConsumo(@RequestBody ReporteRequestDto request) {
        return ResponseEntity.ok(reporteService.generarReporteConsumo(request));
    }

    @PostMapping("/stock")
    public ResponseEntity<List<ReporteStockDto>> generarReporteStock(@RequestBody ReporteRequestDto request) {
        return ResponseEntity.ok(reporteService.generarReporteStock(request));
    }

    @PostMapping("/comparativo")
    public ResponseEntity<List<ReporteChartDto>> generarReporteComparativo(@RequestBody ReporteRequestDto request) {
        return ResponseEntity.ok(reporteService.generarReporteComparativo(request));
    }

    // --- FILTROS ---
    @GetMapping("/filtros/{entidad}")
    public ResponseEntity<List<String>> obtenerFiltros(@PathVariable String entidad) {
        List<String> opciones = new ArrayList<>();
        switch (entidad) {
            case "PROVEEDOR": opciones = productRepo.findAll().stream().map(p -> p.getSupplierName()).distinct().toList(); break;
            case "CATEGORIA": opciones = productRepo.findAll().stream().map(p -> p.getCategory()).distinct().toList(); break;
            case "AREA": opciones = areaRepo.findAll().stream().map(a -> a.getNombre()).distinct().toList(); break;
            case "PRODUCTO": opciones = productRepo.findAll().stream().map(p -> p.getSku() + " - " + p.getName()).distinct().toList(); break;
        }
        return ResponseEntity.ok(opciones);
    }

    // --- MAESTRO DE PRODUCTOS (LÓGICA MEJORADA: HISTORIAL + STOCK) ---
    @GetMapping("/productos-info")
    public ResponseEntity<List<ProductoInfoFilterDto>> obtenerInfoProductos() {
        
        List<InventoryStock> todoStock = stockRepo.findAll();
        List<SalidaHistorial> todoHistorial = salidaRepo.findAll();

        return ResponseEntity.ok(productRepo.findAll().stream()
            .map(p -> {
                Set<String> areasVinculadas = new HashSet<>();

                // 1. Si hay Stock Físico, esa área cuenta
                todoStock.stream()
                    .filter(s -> s.getProduct().getId().equals(p.getId()))
                    .forEach(s -> areasVinculadas.add(s.getAreaDeTrabajo().getNombre()));

                // 2. Si hubo Movimientos (Historial), esas áreas cuentan
                // AQUÍ CUMPLIMOS TU REQUERIMIENTO: Mirar la tabla salida_historial
                todoHistorial.stream()
                    .filter(h -> h.getProductSku().equals(p.getSku()))
                    .forEach(h -> {
                        // Si tuvo un destino real (ej: se envió a Evento), lo agregamos
                        if (h.getAreaDestino() != null && !h.getAreaDestino().equalsIgnoreCase("Consumo Interno")) {
                            areasVinculadas.add(h.getAreaDestino());
                        }
                        // Si se consumió en el origen (ej: General -> General), lo agregamos
                        if (h.getAreaOrigen() != null) {
                            areasVinculadas.add(h.getAreaOrigen());
                        }
                    });

                // Convertimos el Set a un String unido por comas (ej: "Evento, General")
                String areaString = areasVinculadas.isEmpty() ? "Sin Movimiento" : String.join(", ", areasVinculadas);

                return new ProductoInfoFilterDto(
                    p.getSku(),
                    p.getName(),
                    p.getCategory(),
                    p.getSupplierName(),
                    areaString 
                );
            })
            .collect(Collectors.toList()));
    }
    @PostMapping("/venta-diaria")
        public ResponseEntity<List<ReporteVentaDiariaDto>> generarReporteVentaDiaria(@RequestBody ReporteRequestDto request) {
            return ResponseEntity.ok(reporteService.generarReporteVentaDiaria(request));
        }


}


