package com.sistema.cine.init;

import com.sistema.cine.entidad.Usuario;
import com.sistema.cine.entidad.Rol;
import com.sistema.cine.repository.UsuarioRepository;
import com.sistema.cine.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.findByNombre("ROLE_USER").isEmpty()) {
            Rol rolUser = new Rol();
            rolUser.setNombre("ROLE_USER");
            rolRepository.save(rolUser);
        }

        if (rolRepository.findByNombre("ROLE_ADMIN").isEmpty()) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre("ROLE_ADMIN");
            rolRepository.save(rolAdmin);
        }

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setEmail("admin@cineapp.com"); // ← ¡IMPORTANTE!
            admin.setRoles(Set.of(rolRepository.findByNombre("ROLE_ADMIN").get()));
            usuarioRepository.save(admin);
        }

    }
}


