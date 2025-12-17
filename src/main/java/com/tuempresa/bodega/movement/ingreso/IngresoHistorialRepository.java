package com.tuempresa.bodega.movement.ingreso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngresoHistorialRepository extends JpaRepository<IngresoHistorial, Long> {

    // 1. Buscar por N° de Documento exacto
    // Uso: repo.findByNumeroDocumento("12345");
    List<IngresoHistorial> findByNumeroDocumento(String numeroDocumento);

    // 2. Buscar por Proveedor (Búsqueda flexible / "Like")
    // El 'ContainingIgnoreCase' hace que si buscas "Coca", encuentre "Coca Cola" sin importar mayúsculas.
    // Uso: repo.findBySupplierNameContainingIgnoreCase("sopor");
    List<IngresoHistorial> findBySupplierNameContainingIgnoreCase(String supplierName);

    // 3. Buscar por ambos (si alguna vez necesitas validar duplicados específicos)
    List<IngresoHistorial> findByNumeroDocumentoAndSupplierName(String numeroDocumento, String supplierName);
}