package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.area.AreaDeTrabajoRepository;
import com.tuempresa.bodega.inventory.InventoryStock;
import com.tuempresa.bodega.inventory.InventoryStockRepository;
import com.tuempresa.bodega.inventory.dto.StockDisponibleDto;
import com.tuempresa.bodega.movement.salida.dto.GuiaConsumoDto;
import com.tuempresa.bodega.movement.salida.dto.ResumenSalidaDto;
import com.tuempresa.bodega.product.Product;
import com.tuempresa.bodega.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalidaService {

    private final InventoryStockRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final AreaDeTrabajoRepository areaRepository;
    private final SalidaHistorialRepository historialRepository;

    public SalidaService(InventoryStockRepository inventoryRepository, 
                          ProductRepository productRepository, 
                          AreaDeTrabajoRepository areaRepository, 
                          SalidaHistorialRepository historialRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.areaRepository = areaRepository;
        this.historialRepository = historialRepository;
    }

    /**
     * Procesa la guía de consumo completa aplicando la lógica FIFO.
     * Calcula el valor neto real basado en el costo de los lotes consumidos.
     */
    @Transactional
    public void procesarGuiaConsumo(GuiaConsumoDto guia) {
        LocalDate fechaRegistro = guia.getFecha() != null ? guia.getFecha() : LocalDate.now();
        String responsable = (guia.getResponsable() != null && !guia.getResponsable().isEmpty()) 
                             ? guia.getResponsable() : "Usuario Sistema";
        String folio = "GC-" + System.currentTimeMillis();

        for (GuiaConsumoDto.DetalleSalidaDto detalle : guia.getDetalles()) {
            
            // 1. Identificar Área de Origen (Prioriza el del ítem para el Modo General)
            Long idArea = (detalle.getAreaOrigenId() != null) ? detalle.getAreaOrigenId() : guia.getAreaOrigenId();
            AreaDeTrabajo areaOrigen = areaRepository.findById(idArea)
                    .orElseThrow(() -> new RuntimeException("Área no encontrada ID: " + idArea));

            // 2. Buscar Producto por SKU
            Product producto = productRepository.findBySku(detalle.getProductSku())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProductSku()));

            // 3. Obtener lotes ordenados por fecha de ingreso (Lógica FIFO)
            List<InventoryStock> stocks = inventoryRepository.findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(areaOrigen, producto);
            double totalDisponible = stocks.stream().mapToDouble(InventoryStock::getCantidad).sum();

            // Validación de existencia de stock suficiente
            if (totalDisponible < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente de " + producto.getName() + " en " + areaOrigen.getNombre());
            }

            double restante = detalle.getCantidad();
            double valorNetoAcumulado = 0.0;

            // 4. Descontar stock lote por lote
            for (InventoryStock lote : stocks) {
                if (restante <= 0) break;

                double cantidadEnEsteLote = lote.getCantidad();
                double cantidadASacar = Math.min(cantidadEnEsteLote, restante);

                // Acumulamos el costo real del lote
                valorNetoAcumulado += (cantidadASacar * lote.getPrecioCosto());

                if (cantidadEnEsteLote <= restante) {
                    restante -= cantidadEnEsteLote;
                    inventoryRepository.delete(lote);
                } else {
                    lote.setCantidad(cantidadEnEsteLote - restante);
                    inventoryRepository.save(lote);
                    restante = 0;
                }
            }

            // 5. Determinar Destino (Útil para transferencias en Modo General)
            String nombreDestino = "Consumo Interno";
            if (detalle.getAreaDestinoId() != null) {
                nombreDestino = areaRepository.findById(detalle.getAreaDestinoId())
                                .map(AreaDeTrabajo::getNombre).orElse("Consumo Interno");
            }

            // 6. Registrar en el historial
            SalidaHistorial linea = new SalidaHistorial(
                fechaRegistro, folio, producto.getSku(), producto.getName(),
                areaOrigen.getNombre(), nombreDestino, detalle.getCantidad()
            );
            linea.setUsuarioResponsable(responsable);
            linea.setTipoSalida(detalle.getTipoSalida() != null ? detalle.getTipoSalida() : "CONSUMO");
            linea.setValorNeto(valorNetoAcumulado);
            
            historialRepository.save(linea);
        }
    }

    /**
     * Buscador dinámico que soporta el Modo General (búsqueda global si areaId es null).
     */
    public List<StockDisponibleDto> buscarStockParaGuia(Long areaId, String query) {
        // Si no se proporciona areaId (Modo General), busca en todas las áreas
        if (areaId == null) {
            return inventoryRepository.buscarStockGlobalParaGuia(query);
        }
        // De lo contrario, filtra por el área seleccionada
        return inventoryRepository.buscarStockParaGuia(areaId, query);
    }

    /**
     * Obtiene el resumen del historial de salidas.
     */
    public List<ResumenSalidaDto> obtenerResumenHistorial() {
        return historialRepository.findAllResumen();
    }
}