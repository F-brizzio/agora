package com.tuempresa.bodega.auth;

import com.tuempresa.bodega.auth.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired; // <--- IMPORTANTE
import org.springframework.security.crypto.password.PasswordEncoder; // <--- IMPORTANTE

import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Asegúrate que coincida con tu SecurityConfig
public class AuthController {

    private final AuthService authService;

    @Autowired // <--- Inyectamos el encriptador
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        
        // --- CHIVATO GENERADOR DE HASH ---
        // Esto imprimirá en tu consola la contraseña REAL que espera la base de datos
        String hashReal = passwordEncoder.encode("12345");
        System.out.println("==================================================");
        System.out.println("🔑 HASH OFICIAL PARA 12345: " + hashReal);
        System.out.println("==================================================");
        // ---------------------------------

        return ResponseEntity.ok(authService.login(request));
    }
}

