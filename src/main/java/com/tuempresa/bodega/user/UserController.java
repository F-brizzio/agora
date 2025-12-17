package com.tuempresa.bodega.user;

import com.tuempresa.bodega.user.dto.UserRequestDto; // <--- IMPORTANTE: Importar el DTO
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // --- CAMBIO CLAVE: Recibimos UserRequestDto en lugar de User ---
    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    // --- CAMBIO CLAVE: Recibimos UserRequestDto en lugar de User ---
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint especial para confirmar contraseña (Se mantiene igual, estaba perfecto)
    @PostMapping("/verify-security")
    public ResponseEntity<Boolean> verifySecurity(@RequestBody Map<String, String> payload) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        String passwordToCheck = payload.get("password");

        boolean isValid = userService.verifyPassword(currentUsername, passwordToCheck);
        return ResponseEntity.ok(isValid);
    }
}