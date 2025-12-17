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

        // 1. VALIDACIÓN DE ENCABEZADO: Mismo Doc = Mismo Proveedor
        // Buscamos si este número de documento ya existe en el historial
        List<IngresoHistorial> ingresosPrevios = historialRepository.findByNumeroDocumento(request.getNumeroDocumento());

        if (!ingresosPrevios.isEmpty()) {
            String proveedorExistente = ingresosPrevios.get(0).getSupplierName();
            String proveedorNuevo = request.getSupplierName();

            // Validamos que el proveedor sea el mismo (ignorando mayúsculas/minúsculas)
            if (!proveedorExistente.equalsIgnoreCase(proveedorNuevo)) {
                throw new RuntimeException("⛔ Error: El documento N° " + request.getNumeroDocumento() +
                        " ya existe asociado al proveedor '" + proveedorExistente +
                        "'. No puede utilizarse para '" + proveedorNuevo + "'.");
            }
        }

        // Definir responsable (o default)
        String responsable = (request.getResponsable() != null && !request.getResponsable().isEmpty())
                ? request.getResponsable()
                : "Sistema";
        
        LocalDate fechaIngreso = request.getFecha() != null ? request.getFecha() : LocalDate.now();

        // 2. PROCESAR CADA ÍTEM DE LA LISTA
        for (IngresoRequestDto.IngresoItemDto item : request.getItems()) {
            procesarItemIndividual(item, request, fechaIngreso, responsable);
        }
        
        System.out.println("✅ INGRESO MASIVO COMPLETADO. Doc: " + request.getNumeroDocumento());
    }

    private void procesarItemIndividual(IngresoRequestDto.IngresoItemDto item, 
                                        IngresoRequestDto cabecera, 
                                        LocalDate fecha, 
                                        String responsable) {

        // A. Lógica de Producto (Buscar o Crear)
        Optional<Product> productoExistente = productRepository.findBySku(item.getProductSku());
        Product producto;

        if (productoExistente.isPresent()) {
            producto = productoExistente.get();
        } else {
            System.out.println("⚠️ Producto nuevo detectado en lista: " + item.getProductSku());

            // Validaciones básicas para producto nuevo
            if (item.getProductName() == null || item.getProductName().trim().isEmpty()) {
                throw new RuntimeException("El producto SKU " + item.getProductSku() + " es nuevo. Debe ingresar el Nombre.");
            }

            Product nuevo = new Product();
            nuevo.setSku(item.getProductSku());
            nuevo.setName(item.getProductName());
            
            // Usamos los datos del proveedor que vienen en la cabecera
            nuevo.setSupplierName(cabecera.getSupplierName());
            nuevo.setSupplierRut(cabecera.getSupplierRut());

            // Datos específicos del producto nuevo (Valores por defecto si vienen nulos)
            nuevo.setCategory(item.getCategory() != null ? item.getCategory() : "General");
            nuevo.setUnitOfMeasure(item.getUnitOfMeasure() != null ? item.getUnitOfMeasure() : "UNIDAD");
            nuevo.setMinStock(item.getMinStock() != null ? item.getMinStock() : 10.0);
            nuevo.setMaxStock(item.getMaxStock() != null ? item.getMaxStock() : 100.0);
            nuevo.setMaxStorageDays(item.getMaxStorageDays() != null ? item.getMaxStorageDays() : 60);

            producto = productRepository.save(nuevo);
        }

        // B. Buscar Área de Destino
        AreaDeTrabajo area = areaRepository.findById(item.getAreaId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada para el producto: " + item.getProductSku()));

        // C. Crear Stock (Registro físico en inventario)
        InventoryStock nuevoStock = new InventoryStock(
                producto,
                area,
                item.getCantidad(),
                fecha
        );
        inventoryRepository.save(nuevoStock);

        // D. Guardar Historial (Registro de auditoría)
        IngresoHistorial historial = new IngresoHistorial(
                fecha,
                cabecera.getNumeroDocumento(),
                producto.getSku(),
                producto.getName(),
                producto.getCategory(),
                cabecera.getSupplierName(), // Usamos nombre proveedor de cabecera
                cabecera.getSupplierRut(),  // Usamos rut proveedor de cabecera
                area.getNombre(),
                item.getCantidad(),
                item.getCostoUnitario()
        );

        historial.setUsuarioResponsable(responsable);
        historialRepository.save(historial);
    }
}