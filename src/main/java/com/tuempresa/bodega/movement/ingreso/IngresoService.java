package com.tuempresa.bodega.movement.ingreso;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.area.AreaDeTrabajoRepository;
import com.tuempresa.bodega.inventory.InventoryStock;
import com.tuempresa.bodega.inventory.InventoryStockRepository;
import com.tuempresa.bodega.movement.ingreso.dto.IngresoRequestDto;
import com.tuempresa.bodega.product.Product;
import com.tuempresa.bodega.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IngresoService {

    private final InventoryStockRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final AreaDeTrabajoRepository areaRepository;
    private final IngresoHistorialRepository historialRepository;

    public IngresoService(InventoryStockRepository inventoryRepository, 
                          ProductRepository productRepository, 
                          AreaDeTrabajoRepository areaRepository, 
                          IngresoHistorialRepository historialRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.areaRepository = areaRepository;
        this.historialRepository = historialRepository;
    }

    @Transactional
    public void procesarIngreso(IngresoRequestDto request) {
        // Validación de duplicados (Lógica defensiva)
        List<IngresoHistorial> ingresosPrevios = historialRepository.findByNumeroDocumento(request.getNumeroDocumento());

        if (!ingresosPrevios.isEmpty()) {
            String proveedorExistente = ingresosPrevios.get(0).getSupplierName();
            // Normalizamos strings para evitar errores por mayúsculas/minúsculas
            if (!proveedorExistente.trim().equalsIgnoreCase(request.getSupplierName().trim())) {
                throw new RuntimeException("⛔ Error: El documento N° " + request.getNumeroDocumento() +
                        " ya existe asociado al proveedor '" + proveedorExistente + "'.");
            }
        }

        String responsable = (request.getResponsable() != null && !request.getResponsable().isEmpty())
                ? request.getResponsable() : "Sistema";
        
        LocalDate fechaIngreso = request.getFecha() != null ? request.getFecha() : LocalDate.now();

        for (IngresoRequestDto.IngresoItemDto item : request.getItems()) {
            procesarItemIndividual(item, request, fechaIngreso, responsable);
        }
    }

    private void procesarItemIndividual(IngresoRequestDto.IngresoItemDto item, 
                                        IngresoRequestDto cabecera, 
                                        LocalDate fecha, 
                                        String responsable) {

        Optional<Product> productoExistente = productRepository.findBySku(item.getProductSku());
        Product producto = productoExistente.orElseGet(() -> {
            Product nuevo = new Product();
            nuevo.setSku(item.getProductSku());
            nuevo.setName(item.getProductName());
            nuevo.setSupplierName(cabecera.getSupplierName());
            nuevo.setSupplierRut(cabecera.getSupplierRut());
            nuevo.setCategory(item.getCategory() != null ? item.getCategory() : "General");
            nuevo.setUnitOfMeasure(item.getUnitOfMeasure() != null ? item.getUnitOfMeasure() : "UNIDAD");
            nuevo.setMinStock(item.getMinStock() != null ? item.getMinStock() : 10.0);
            nuevo.setMaxStock(item.getMaxStock() != null ? item.getMaxStock() : 100.0);
            nuevo.setMaxStorageDays(item.getMaxStorageDays() != null ? item.getMaxStorageDays() : 60);
            return productRepository.save(nuevo);
        });

        AreaDeTrabajo area = areaRepository.findById(item.getAreaId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada para el producto: " + item.getProductSku()));

        // Crear lote de inventario
        InventoryStock nuevoStock = new InventoryStock(producto, area, item.getCantidad(), fecha);
        inventoryRepository.save(nuevoStock);

        // Guardar historial
        IngresoHistorial historial = new IngresoHistorial(
                fecha, cabecera.getNumeroDocumento(), producto.getSku(), producto.getName(), producto.getCategory(),
                cabecera.getSupplierName(), cabecera.getSupplierRut(), area.getNombre(), item.getCantidad(), item.getCostoUnitario()
        );

        historial.setUsuarioResponsable(responsable);
        historialRepository.save(historial);
    }

    // --- LÓGICA CORREGIDA PARA ACTUALIZACIÓN ---
    @Transactional
    public IngresoHistorial actualizarIngreso(Long id, IngresoHistorial nuevosDatos) {
        // 1. Obtener el registro histórico original
        IngresoHistorial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro histórico no encontrado"));

        // 2. Verificar si cambió la cantidad para ajustar el stock físico (Lógica Delta)
        Double cantidadAnterior = historial.getCantidad();
        Double cantidadNueva = nuevosDatos.getCantidad();

        // Usamos equals con tolerancia para doubles o comparación directa si no son nulos
        if (cantidadAnterior != null && !cantidadAnterior.equals(cantidadNueva)) {
            double diferencia = cantidadNueva - cantidadAnterior; 

            // Buscamos el stock físico asociado usando SKU, Fecha y Área
            List<InventoryStock> stocks = inventoryRepository.findByProductSku(historial.getProductSku());
            
            InventoryStock stockFisico = stocks.stream()
    .filter(s -> s.getFechaIngreso().equals(historial.getFecha())) 
    
    // CORRECCIÓN AQUÍ: Usamos .getAreaDeTrabajo() en lugar de .getArea()
    .filter(s -> s.getAreaDeTrabajo().getNombre().equalsIgnoreCase(historial.getAreaNombre())) 
    
    .findFirst()
    .orElseThrow(() -> new RuntimeException("No se encontró el lote de inventario original..."));
            // Validar que no quede negativo
            if (stockFisico.getCantidad() + diferencia < 0) {
                throw new RuntimeException("⛔ No se puede reducir la cantidad: El stock físico actual (" 
                    + stockFisico.getCantidad() + ") es insuficiente.");
            }

            // Aplicar ajuste
            stockFisico.setCantidad(stockFisico.getCantidad() + diferencia);
            inventoryRepository.save(stockFisico);
        }

        // 3. Actualizar datos del registro histórico
        // Solo actualizamos los datos base. Los totales (Neto/Bruto) se calcularán solos gracias a tu Entidad.
        historial.setCantidad(nuevosDatos.getCantidad());
        historial.setCostoUnitario(nuevosDatos.getCostoUnitario());
        historial.setNumeroDocumento(nuevosDatos.getNumeroDocumento());
        historial.setSupplierRut(nuevosDatos.getSupplierRut());
        historial.setSupplierName(nuevosDatos.getSupplierName());
        
        // NO hace falta setTotalNeto ni setTotalBruto, tu clase los calcula automágicamente.

        return historialRepository.save(historial);
    }
}