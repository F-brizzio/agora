package com.tuempresa.bodega.product;

import com.tuempresa.bodega.product.dto.ProductRequestDto;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequestDto dto) {
        Product product = new Product();
        
        product.setSku(dto.getProductId());
        product.setName(dto.getNombre());
        product.setCategory(dto.getCategoria());
        product.setUnitOfMeasure(dto.getUnidadDeMedida());
        product.setSupplierName(dto.getProveedorNombre());
        product.setSupplierRut(dto.getProveedorRut());
        product.setDescription(dto.getNombre()); 

        // Stock Mínimo
        if (dto.getStockMinimo() != null) {
            product.setMinStock(dto.getStockMinimo().doubleValue());
        } else {
            product.setMinStock(0.0);
        }

        // Stock Máximo
        if (dto.getStockMaximo() != null) {
            product.setMaxStock(dto.getStockMaximo().doubleValue());
        } else {
            product.setMaxStock(0.0);
        }
        
        // Tiempo Bodega
        if (dto.getTiempoMaximoBodega() != null) {
            product.setMaxStorageDays(dto.getTiempoMaximoBodega());
        } else {
            product.setMaxStorageDays(0);
        }

        // --- ELIMINADO: product.setPrice(...) ---

        return productRepository.save(product);
    }
}