package com.sistema.cine.repository;

import com.sistema.cine.entidad.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    
    
    
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByResetToken(String resetToken);
    
}
