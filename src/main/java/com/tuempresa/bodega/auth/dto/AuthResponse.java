package com.tuempresa.bodega.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty; // Asegúrate de tener Jackson
import java.util.Set;

public class AuthResponse {

    private String token;
    private String username;
    private String fullName;
    private String role;

    // Usamos @JsonProperty para OBLIGAR a que este campo aparezca en el JSON
    @JsonProperty("accesos")
    private Set<String> accesos;

    // CONSTRUCTOR CON 5 PARÁMETROS
    public AuthResponse(String token, String username, String fullName, String role, Set<String> accesos) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.accesos = accesos;
    }

    // --- GETTERS (OBLIGATORIOS) ---
    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    
    // ¡SIN ESTE GETTER, NO FUNCIONA!
    public Set<String> getAccesos() { return accesos; }
}