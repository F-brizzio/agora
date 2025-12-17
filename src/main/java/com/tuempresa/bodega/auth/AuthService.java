package com.tuempresa.bodega.auth;

import com.tuempresa.bodega.auth.dto.LoginRequest;
import com.tuempresa.bodega.user.UserRepository;
import com.tuempresa.bodega.config.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap; // <--- Importar
import java.util.Map;     // <--- Importar

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    // CAMBIO: Ahora devuelve un Mapa genérico, no una clase específica
    public Map<String, Object> login(LoginRequest request) { 
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        var jwtToken = jwtService.generateToken(user);

        // --- DEBUG ---
        System.out.println("DEBUG FINAL: Accesos de " + user.getUsername() + ": " + user.getAccesos());

        // --- CONSTRUIMOS LA RESPUESTA MANUALMENTE ---
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("token", jwtToken);
        respuesta.put("username", user.getUsername());
        respuesta.put("fullName", user.getFullName());
        respuesta.put("role", user.getRole().name());
        
        // ¡Aquí metemos la lista directamente!
        respuesta.put("accesos", user.getAccesos()); 

        return respuesta;
    }
}