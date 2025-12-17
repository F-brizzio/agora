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

    // --- 1. PROCESAR GUÍA DE CONSUMO (POST) ---
    @Transactional
    public void procesarGuiaConsumo(GuiaConsumoDto guia) {
        
        // 1. Validar Fecha (Si es null, usa hoy)
        LocalDate fechaRegistro = guia.getFecha();
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }

        // 2. Obtener Responsable (Del DTO)
        String responsable = (guia.getResponsable() != null && !guia.getResponsable().isEmpty()) 
                             ? guia.getResponsable() 
                             : "Usuario Sistema";

        // 3. Validar Área Origen
        AreaDeTrabajo areaOrigen = areaRepository.findById(guia.getAreaOrigenId())
                .orElseThrow(() -> new RuntimeException("Área de origen no encontrada"));

        String folio = "GC-" + System.currentTimeMillis(); 

        // 4. Procesar Detalles (Iteramos por cada producto/línea)
        for (GuiaConsumoDto.DetalleSalidaDto detalle : guia.getDetalles()) {
            
            Product producto = productRepository.findBySku(detalle.getProductSku())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProductSku()));

            // Lógica de Destino: Si sale de Bodega, registramos para quién fue.
            String nombreDestino = "Consumo Interno"; 
            if (areaOrigen.getNombre().equalsIgnoreCase("Sin Asignar") || areaOrigen.getNombre().equalsIgnoreCase("Bodega Central")) {
                if (detalle.getAreaDestinoId() != null) {
                     AreaDeTrabajo destino = areaRepository.findById(detalle.getAreaDestinoId()).orElse(null);
                     if(destino != null) nombreDestino = destino.getNombre();
                }
            } else {
                 nombreDestino = areaOrigen.getNombre(); 
            }

            // Lógica FIFO (Descuento de Stock)
            List<InventoryStock> stocks = inventoryRepository.findByAreaDeTrabajoAndProductOrderByFechaIngresoAsc(areaOrigen, producto);
            double totalDisponible = stocks.stream().mapToDouble(InventoryStock::getCantidad).sum();
            
            if (totalDisponible < detalle.getCantidad()) {
                 throw new RuntimeException("Stock insuficiente para " + producto.getName() + ". Disponible: " + totalDisponible);
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

            // 5. Guardar Historial con TIPO DE SALIDA (Merma o Consumo)
            
            // Leemos el tipo desde el detalle (Si el front no manda nada, es CONSUMO)
            String tipoRegistro = (detalle.getTipoSalida() != null && !detalle.getTipoSalida().isEmpty()) 
                                  ? detalle.getTipoSalida() 
                                  : "CONSUMO";

            SalidaHistorial lineaHistorial = new SalidaHistorial(
                fechaRegistro,
                folio,                  
                producto.getSku(),
                producto.getName(),
                areaOrigen.getNombre(),
                nombreDestino,          
                detalle.getCantidad()
            );
            
            // Asignamos los campos adicionales
            lineaHistorial.setUsuarioResponsable(responsable);
            lineaHistorial.setTipoSalida(tipoRegistro); // <--- AQUÍ SE GUARDA SI FUE MERMA
            
            historialRepository.save(lineaHistorial);
        }
        
        System.out.println("✅ GUÍA PROCESADA. FOLIO: " + folio + " | Responsable: " + responsable);
    }

    // --- 2. OBTENER HISTORIAL (GET) ---
    public List<SalidaHistorial> obtenerHistorialCompleto() {
        return historialRepository.findAll();
    }
}