package com.tuempresa.bodega.product;

import com.tuempresa.bodega.product.dto.ProductRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;
    private final ProductService productService;

    public ProductController(ProductRepository productRepo, ProductService productService) {
        this.productRepo = productRepo;
        this.productService = productService;
    }

    // 1. LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepo.findAll());
    }

    // 2. CREAR (Delegamos al servicio que ya arreglamos)
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    // 3. EDITAR (Actualizamos mapeo sin precio y con nombres correctos)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto dto) {
        return productRepo.findById(id)
                .map(existing -> {
                    // --- Strings (Español DTO -> Inglés Entidad) ---
                    existing.setName(dto.getNombre());
                    existing.setCategory(dto.getCategoria());
                    existing.setUnitOfMeasure(dto.getUnidadDeMedida());
                    existing.setSupplierName(dto.getProveedorNombre());
                    existing.setSupplierRut(dto.getProveedorRut());
                    existing.setDescription(dto.getNombre()); // Usamos nombre como descripción

                    // --- Números (Conversión Segura a Double) ---
                    
                    // Stock Mínimo
                    if (dto.getStockMinimo() != null) {
                        existing.setMinStock(dto.getStockMinimo().doubleValue());
                    } else {
                        existing.setMinStock(0.0);
                    }

                    // Stock Máximo
                    if (dto.getStockMaximo() != null) {
                        existing.setMaxStock(dto.getStockMaximo().doubleValue());
                    } else {
                        existing.setMaxStock(0.0);
                    }

                    // Tiempo Bodega
                    if (dto.getTiempoMaximoBodega() != null) {
                        existing.setMaxStorageDays(dto.getTiempoMaximoBodega());
                    } else {
                        existing.setMaxStorageDays(0);
                    }

                    // --- ELIMINADO: existing.setPrice(...) ---
                    
                    return ResponseEntity.ok(productRepo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}