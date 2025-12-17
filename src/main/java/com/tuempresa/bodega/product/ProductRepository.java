package com.tuempresa.bodega.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // Importante

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Definimos el método para buscar por SKU
    Optional<Product> findBySku(String sku);
}