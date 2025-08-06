package com.sistema.cine.service;

import java.util.List;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistema.cine.entidad.Rol;
import com.sistema.cine.entidad.Usuario;
import com.sistema.cine.repository.RolRepository;
import com.sistema.cine.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registrarUsuarioConRolUser(Usuario usuario) {
        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Buscar rol ROLE_USER, si no existe, lanzar excepción
        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("Rol 'ROLE_USER' no encontrado"));

        // Asignar rol USER al usuario
        usuario.setRoles(new HashSet<>(List.of(rolUser)));

        // Guardar usuario
        usuarioRepository.save(usuario);
    }

    public boolean existeUsuarioPorUsername(String username) {
        // Verificar existencia de usuario por username
        return usuarioRepository.findByUsername(username).isPresent();
    }
}
