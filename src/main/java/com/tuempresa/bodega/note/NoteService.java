package com.tuempresa.bodega.note;

import com.tuempresa.bodega.movement.ingreso.IngresoHistorial;
import com.tuempresa.bodega.movement.ingreso.IngresoHistorialRepository;
import com.tuempresa.bodega.product.Product;
import com.tuempresa.bodega.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final IngresoHistorialRepository ingresoRepo;
    private final ProductRepository productRepo;

    public NoteService(NoteRepository noteRepository, IngresoHistorialRepository ingresoRepo, ProductRepository productRepo) {
        this.noteRepository = noteRepository;
        this.ingresoRepo = ingresoRepo;
        this.productRepo = productRepo;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note saveNote(Note note) {
        // LÓGICA ESPECIAL: MERMA
        if ("Merma".equals(note.getCategory()) && note.getProductSku() != null && note.getQuantity() > 0) {
            
            // --- CORRECCIÓN AQUÍ: USAMOS .orElse(null) ---
            Product p = productRepo.findBySku(note.getProductSku()).orElse(null);
            // ---------------------------------------------
            
            if (p != null) {
                note.setProductName(p.getName());
            }

            // Calcular Costo FIFO
            List<IngresoHistorial> ingresos = ingresoRepo.findAll().stream()
                .filter(i -> i.getProductSku().equals(note.getProductSku()))
                .sorted(Comparator.comparing(IngresoHistorial::getFecha))
                .collect(Collectors.toList());

            double costoCalculado = calcularCostoFIFO(note.getQuantity(), ingresos);
            note.setCalculatedCost(costoCalculado);
        }

        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    private double calcularCostoFIFO(double cantidadNecesaria, List<IngresoHistorial> ingresosOrdenados) {
        double costoAcumulado = 0;
        double pendientes = cantidadNecesaria;

        for (IngresoHistorial ingreso : ingresosOrdenados) {
            if (pendientes <= 0) break;
            double pUnit = (ingreso.getCantidad() > 0) ? ingreso.getTotalBruto() / ingreso.getCantidad() : 0;
            double tomar = Math.min(ingreso.getCantidad(), pendientes);
            
            costoAcumulado += (tomar * pUnit);
            pendientes -= tomar;
        }
        return costoAcumulado;
    }
}