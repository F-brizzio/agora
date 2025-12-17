package com.tuempresa.bodega.user.dto;

import java.util.Set; // <--- Importante para los permisos

public class UserRequestDto {
    private String fullName;
    private String username;
    private String password;
    private String rut;
    private String role;
    
    // --- ESTE ES EL CAMPO CLAVE PARA LOS PERMISOS ---
    private Set<String> accesos; 

    public UserRequestDto() {
    }

    // --- GETTERS Y SETTERS ---

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Set<String> getAccesos() { return accesos; }
    public void setAccesos(Set<String> accesos) { this.accesos = accesos; }
}