package com.tuempresa.bodega.area;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    private final AreaDeTrabajoRepository areaRepository;

    public AreaController(AreaDeTrabajoRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @GetMapping
    public ResponseEntity<List<AreaDeTrabajo>> listarAreas() {
        return ResponseEntity.ok(areaRepository.findAll());
    }
    
    @PostMapping
    public ResponseEntity<AreaDeTrabajo> crearArea(@RequestBody AreaDeTrabajo area) {
        return ResponseEntity.ok(areaRepository.save(area));
    }
}