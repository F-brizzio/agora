package com.tuempresa.bodega.movement.salida;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.area.AreaDeTrabajoRepository;
import com.tuempresa.bodega.inventory.InventoryStock;
import com.tuempresa.bodega.inventory.InventoryStockRepository;
import com.tuempresa.bodega.movement.salida.dto.GuiaConsumoDto;
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

    public SalidaService(InventoryStockRepository inventoryRepository, ProductRepository productRepository, AreaDeTrabajoRepository areaRepository, SalidaHistorialRepository historialRepository) {
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
            
            // A. DETERMINAR EL ÁREA DE ORIGEN
            // Calculamos el ID. Si el detalle es null, usamos el de la guía.
            Long idCalculado = detalle.getAreaOrigenId();
            if (idCalculado == null) {
                idCalculado = guia.getAreaOrigenId();
            }

            if (idCalculado == null) {
                throw new RuntimeException("No se especificó un Área de Origen para el producto: " + detalle.getProductSku());
            }

            // TRUCO PARA JAVA: Creamos una variable "final" auxiliar para usar dentro de la Lambda
            final Long idParaConsulta = idCalculado;

            AreaDeTrabajo areaOrigen = areaRepository.findById(idParaConsulta)
                    .orElseThrow(() -> new RuntimeException("Área de origen no encontrada ID: " + idParaConsulta));

            // B. BUSCAR PRODUCTO
            Product producto = productRepository.findBySku(detalle.getProductSku())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProductSku()));

            // C. LOGICA DE DESTINO
            String nombreDestino = "Consumo Interno";
            boolean esBodega = areaOrigen.getNombre().equalsIgnoreCase("Bodega Central") || areaOrigen.getNombre().equalsIgnoreCase("Sin Asignar");
            
            if (esBodega && detalle.getAreaDestinoId() != null) {
                AreaDeTrabajo destino = areaRepository.findById(detalle.getAreaDestinoId()).orElse(null);
                if(destino != null) nombreDestino = destino.getNombre();
            } else if (!esBodega) {
                nombreDestino = areaOrigen.getNombre(); 
            }

            // D. LOGICA FIFO
            List<InventoryStock> stocks = inventoryRepository.findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(areaOrigen, producto);
            double totalDisponible = stocks.stream().mapToDouble(InventoryStock::getCantidad).sum();

            if (totalDisponible < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente en " + areaOrigen.getNombre() + " para " + producto.getName());
            }

            double restante = detalle.getCantidad();
            for (InventoryStock lote : stocks) {
                if (restante <= 0) break;
                double disp = lote.getCantidad();

                if (disp <= restante) {
                    restante -= disp;
                    inventoryRepository.delete(lote);
                } else {
                    lote.setCantidad(disp - restante);
                    inventoryRepository.save(lote);
                    restante = 0;
                }
            }

            // E. GUARDAR HISTORIAL
            String tipoRegistro = (detalle.getTipoSalida() != null && !detalle.getTipoSalida().isEmpty())
                                  ? detalle.getTipoSalida() : "CONSUMO";

            SalidaHistorial lineaHistorial = new SalidaHistorial(
                fechaRegistro,
                folio,
                producto.getSku(),
                producto.getName(),
                areaOrigen.getNombre(),
                nombreDestino,
                detalle.getCantidad()
            );
            
            lineaHistorial.setUsuarioResponsable(responsable);
            lineaHistorial.setTipoSalida(tipoRegistro);
            
            historialRepository.save(lineaHistorial);
        }
        
        System.out.println("✅ GUÍA PROCESADA CORRECTAMENTE. FOLIO: " + folio);
    }

    public List<SalidaHistorial> obtenerHistorialCompleto() {
        return historialRepository.findAll(); 
    }
}