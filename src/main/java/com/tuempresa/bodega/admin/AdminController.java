package com.tuempresa.bodega.admin; // Asegúrate que el paquete sea el correcto

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin") // La URL base protegida que definimos
public class AdminController {

    /**
     * Un endpoint de prueba para verificar que la seguridad de ADMIN funciona.
     * Se accede vía GET a http://localhost:8080/api/admin/hello
     */
    @GetMapping("/hello")
    public String sayHelloAdmin() {
        // Si llegas aquí, significa que tu token es válido y eres ADMIN.
        return "¡Hola Admin! Tienes acceso a esta ruta protegida.";
    }

}