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
            Long idArea = (detalle.getAreaOrigenId() != null) ? detalle.getAreaOrigenId() : guia.getAreaOrigenId();
            AreaDeTrabajo areaOrigen = areaRepository.findById(idArea)
                    .orElseThrow(() -> new RuntimeException("Área no encontrada ID: " + idArea));

            Product producto = productRepository.findBySku(detalle.getProductSku())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProductSku()));

            List<InventoryStock> stocks = inventoryRepository.findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(areaOrigen, producto);
            double totalDisponible = stocks.stream().mapToDouble(InventoryStock::getCantidad).sum();

            if (totalDisponible < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente de " + producto.getName() + " en " + areaOrigen.getNombre());
            }

            double restante = detalle.getCantidad();
            double valorNetoAcumulado = 0.0;

            for (InventoryStock lote : stocks) {
                if (restante <= 0) break;
                double cantidadASacar = Math.min(lote.getCantidad(), restante);
                valorNetoAcumulado += (cantidadASacar * lote.getPrecioCosto());

                if (lote.getCantidad() <= restante) {
                    restante -= lote.getCantidad();
                    inventoryRepository.delete(lote);
                } else {
                    lote.setCantidad(lote.getCantidad() - restante);
                    inventoryRepository.save(lote);
                    restante = 0;
                }
            }

            String nombreDestino = "Consumo Interno";
            if (detalle.getAreaDestinoId() != null) {
                nombreDestino = areaRepository.findById(detalle.getAreaDestinoId())
                                .map(AreaDeTrabajo::getNombre).orElse("Consumo Interno");
            }

            SalidaHistorial linea = new SalidaHistorial(fechaRegistro, folio, producto.getSku(), producto.getName(), areaOrigen.getNombre(), nombreDestino, detalle.getCantidad());
            linea.setUsuarioResponsable(responsable);
            linea.setTipoSalida(detalle.getTipoSalida() != null ? detalle.getTipoSalida() : "CONSUMO");
            linea.setValorNeto(valorNetoAcumulado);
            historialRepository.save(linea);
        }
    }

    public List<?> buscarStockParaGuia(Long areaId, String query) {
        // Lógica especial: Si no hay areaId o es el de "General", buscar en toda la bodega
        if (areaId == null) {
            return inventoryRepository.buscarStockGlobalParaGuia(query);
        }
        return inventoryRepository.buscarStockParaGuia(areaId, query);
    }

    public List<ResumenSalidaDto> obtenerResumenHistorial() {
        return historialRepository.findAllResumen();
    }
}