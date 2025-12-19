package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.area.AreaDeTrabajoRepository;
import com.tuempresa.bodega.inventory.InventoryStock;
import com.tuempresa.bodega.inventory.InventoryStockRepository;
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

    @Transactional
    public void procesarGuiaConsumo(GuiaConsumoDto guia) {
        LocalDate fechaRegistro = guia.getFecha() != null ? guia.getFecha() : LocalDate.now();
        String responsable = (guia.getResponsable() != null && !guia.getResponsable().isEmpty()) 
                             ? guia.getResponsable() : "Usuario Sistema";
        String folio = "GC-" + System.currentTimeMillis();

        for (GuiaConsumoDto.DetalleSalidaDto detalle : guia.getDetalles()) {
            
            // 1. Identificar Área de Origen
            Long idArea = (detalle.getAreaOrigenId() != null) ? detalle.getAreaOrigenId() : guia.getAreaOrigenId();
            AreaDeTrabajo areaOrigen = areaRepository.findById(idArea)
                    .orElseThrow(() -> new RuntimeException("Área no encontrada ID: " + idArea));

            // 2. Buscar Producto
            Product producto = productRepository.findBySku(detalle.getProductSku())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProductSku()));

            // 3. Lógica FIFO y Cálculo de Valor Neto Real
            List<InventoryStock> stocks = inventoryRepository.findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(areaOrigen, producto);
            double totalDisponible = stocks.stream().mapToDouble(InventoryStock::getCantidad).sum();

            if (totalDisponible < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente de " + producto.getName() + " en " + areaOrigen.getNombre());
            }

            double restante = detalle.getCantidad();
            double valorNetoAcumulado = 0.0; // Aquí sumaremos el costo real de cada lote usado

            for (InventoryStock lote : stocks) {
                if (restante <= 0) break;

                double cantidadEnEsteLote = lote.getCantidad();
                double cantidadASacar = Math.min(cantidadEnEsteLote, restante);

                // SUMAMOS AL VALOR NETO: (Cantidad sacada de este lote * Su precio de costo original)
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

            // 4. Determinar Destino
            String nombreDestino = "Consumo Interno";
            if (detalle.getAreaDestinoId() != null) {
                nombreDestino = areaRepository.findById(detalle.getAreaDestinoId())
                                .map(AreaDeTrabajo::getNombre).orElse("Consumo Interno");
            }

            // 5. Guardar en Historial con el Valor Neto Real calculado
            SalidaHistorial linea = new SalidaHistorial(
                fechaRegistro, folio, producto.getSku(), producto.getName(),
                areaOrigen.getNombre(), nombreDestino, detalle.getCantidad()
            );
            linea.setUsuarioResponsable(responsable);
            linea.setTipoSalida(detalle.getTipoSalida() != null ? detalle.getTipoSalida() : "CONSUMO");
            linea.setValorNeto(valorNetoAcumulado); // <--- VALOR REAL SEGÚN LOTES
            
            historialRepository.save(linea);
        }
    }

    // Método para el Buscador Dinámico del Frontend
    public List<InventoryStock> buscarStockPorAreaYNombre(Long areaId, String query) {
        AreaDeTrabajo area = areaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));
        return inventoryRepository.findByAreaDeTrabajoAndProduct_NameContainingIgnoreCase(area, query);
    }

    // Listar resumen agrupado (para la tabla principal del historial)
    public List<ResumenSalidaDto> obtenerResumenHistorial() {
        return historialRepository.findAllResumen();
    }
}