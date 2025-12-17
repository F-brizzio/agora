package com.tuempresa.bodega.alert;

import com.tuempresa.bodega.alert.dto.AlertaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    public ResponseEntity<List<AlertaDto>> obtenerAlertas() {
        return ResponseEntity.ok(alertaService.generarAlertas());
    }
}