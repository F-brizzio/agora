package com.tuempresa.bodega.area;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AreaService {

    private final AreaDeTrabajoRepository areaRepository;

    // Inyección de dependencias manual
    public AreaService(AreaDeTrabajoRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    // Lógica para listar todas las áreas
    public List<AreaDeTrabajo> listarTodas() {
        return areaRepository.findAll();
    }

    // Lógica para guardar una nueva área
    public AreaDeTrabajo guardarArea(AreaDeTrabajo area) {
        // Aquí podrías validar si el nombre ya existe antes de guardar
        return areaRepository.save(area);
    }
    
    // Más adelante agregaremos aquí el método "borrar" con validaciones
}