package com.sistema.cine.controller;

import com.sistema.cine.entidad.Usuario;

import com.sistema.cine.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthControlador {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        HttpServletRequest request,
                                        Model model) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = UUID.randomUUID().toString();
            usuario.setResetToken(token);
            usuario.setTokenExpiry(LocalDateTime.now().plusHours(1));
            usuarioRepository.save(usuario);

            String resetLink = request.getRequestURL().toString().replace(request.getServletPath(), "") +
                               "/reset-password?token=" + token;

            try {
                // Enviar correo
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("Restablecer contraseña - CineApp");
                message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña:\n" + resetLink);
                mailSender.send(message);

                model.addAttribute("message", "Se ha enviado un correo con instrucciones.");
            } catch (Exception e) {
                model.addAttribute("message", "Ocurrió un error al enviar el correo: " + e.getMessage());
            }

        } else {
            model.addAttribute("message", "No se encontró ningún usuario con ese correo.");
        }

        return "forgot-password";
    }

}
