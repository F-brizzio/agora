package com.tuempresa.bodega.config;

import com.tuempresa.bodega.area.AreaDeTrabajo;
import com.tuempresa.bodega.area.AreaDeTrabajoRepository;
import com.tuempresa.bodega.user.Role;
import com.tuempresa.bodega.user.User;
import com.tuempresa.bodega.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final AreaDeTrabajoRepository areaRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, AreaDeTrabajoRepository areaRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.areaRepository = areaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // 1. CREAR USUARIO ADMIN (Si no existe)
        if (userRepository.count() == 0) {
           // En DataLoader.java
            User admin = new User(
                "admin2",
                passwordEncoder.encode("12345"),
                "Super Administrador",
                "11.111.111-1", 
                Role.ADMIN
            );
            userRepository.save(admin);

            System.out.println("✅ USUARIO ADMIN CREADO:");
            System.out.println(" -> User: admin2 / Pass: 12345");
        }

        // 2. CREAR ÁREAS DE TRABAJO (Si no existen)
        if (areaRepository.count() == 0) {
            List<String> areasGuia = List.of(
                "Coffee", "Casino", "Administración", "General", 
                "Sin asignar", "Residencia", "Alojamiento", "Evento"
            );

            for (String nombre : areasGuia) {
                areaRepository.save(new AreaDeTrabajo(nombre));
            }
            System.out.println("✅ ÁREAS DE TRABAJO CREADAS (" + areasGuia.size() + ")");
        }
    }
}