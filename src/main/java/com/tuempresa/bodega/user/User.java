package com.tuempresa.bodega.user;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String rut;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    // --- ESTO ES LO IMPORTANTE ---
    // FetchType.EAGER obliga a traer los datos SIEMPRE
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_accesos", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "modulo_key")
    private Set<String> accesos = new HashSet<>();

    public User() {}

    public User(String username, String password, String fullName, String rut, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.rut = rut;
        this.role = role;
    }

    // --- GETTERS (CRUCIALES) ---
    public Set<String> getAccesos() { return accesos; }
    public void setAccesos(Set<String> accesos) { this.accesos = accesos; }
    
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getRut() { return rut; }
    public Role getRole() { return role; }
    
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRut(String rut) { this.rut = rut; }
    public void setRole(Role role) { this.role = role; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}