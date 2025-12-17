package com.tuempresa.bodega.user;

import com.tuempresa.bodega.user.dto.UserRequestDto; // <--- Importamos el DTO
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // --- CREAR USUARIO (CON PERMISOS) ---
    public User createUser(UserRequestDto request) {
        User user = new User();
        
        // 1. Mapeo de datos básicos
        user.setFullName(request.getFullName());
        user.setRut(request.getRut());
        user.setUsername(request.getUsername());
        
        // 2. Rol (Default a USER si viene null)
        if (request.getRole() != null) {
            user.setRole(Role.valueOf(request.getRole()));
        } else {
            user.setRole(Role.USER);
        }

        // 3. Contraseña Encriptada
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 4. GUARDAR LOS ACCESOS (La parte clave)
        if (request.getAccesos() != null) {
            user.setAccesos(request.getAccesos());
        }

        return userRepository.save(user);
    }

    // --- ACTUALIZAR USUARIO (CON PERMISOS) ---
    public User updateUser(Long id, UserRequestDto request) {
        return userRepository.findById(id).map(user -> {
            
            // Actualizar datos solo si vienen en la petición (no son null)
            if(request.getFullName() != null) user.setFullName(request.getFullName());
            if(request.getRut() != null) user.setRut(request.getRut());
            if(request.getUsername() != null) user.setUsername(request.getUsername());
            
            if(request.getRole() != null) {
                user.setRole(Role.valueOf(request.getRole()));
            }

            // Si trae contraseña nueva, la encriptamos y actualizamos
            if(request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            // --- ACTUALIZACIÓN DE ACCESOS / PERMISOS ---
            if (request.getAccesos() != null) {
                // Borramos los permisos viejos y ponemos los nuevos
                // Esto maneja tanto agregar como quitar checkboxes
                user.getAccesos().clear();
                user.getAccesos().addAll(request.getAccesos());
            }
            // -------------------------------------------

            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Verificación de contraseña para acciones sensibles
    public boolean verifyPassword(String username, String rawPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return false;
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}