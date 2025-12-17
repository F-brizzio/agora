package com.tuempresa.bodega.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; 
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. DESHABILITAR CSRF: Es obligatorio en APIs REST/JWT para evitar el 403 en POST.
            .csrf(csrf -> csrf.disable()) 
            
            // 2. CONFIGURACIÓN DE CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 3. DEFINICIÓN DE PERMISOS
            .authorizeHttpRequests(auth -> auth
                // Permitimos el acceso total a todas las rutas de autenticación, incluyendo /api/auth/login, SIN token JWT.
                .requestMatchers("/api/auth/**").permitAll() 
                // Permitimos rutas antiguas si existen.
                .requestMatchers("/auth/**").permitAll() 
                // Cualquier otra solicitud (incluyendo /api/**) debe estar autenticada con un token JWT.
                .anyRequest().authenticated()
            )
            
            // 4. POLÍTICA DE SESIÓN: Indicamos que es STATELESS (sin estado), obligatorio para JWT.
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 5. ASIGNACIÓN DE PROVIDER Y FILTRO JWT
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // CORRECCIÓN: Se agrega el puerto 8080 en los orígenes permitidos para evitar un posible 403 por CORS.
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://45.236.130.90",
            "http://45.236.130.90:80",
            "http://45.236.130.90:8080", 
            "https://gestion.centroelagora.cl"

            // <--- Importante: Incluir el puerto del backend si el frontend puede correr allí.
        )); 
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Aseguramos que se permitan las cabeceras de Content-Type y Authorization (para el JWT).
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); 
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}