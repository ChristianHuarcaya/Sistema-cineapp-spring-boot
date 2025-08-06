package com.sistema.cine.controller;

import com.sistema.cine.entidad.Usuario;
import com.sistema.cine.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroControlador {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model model) {
	    model.addAttribute("usuario", new Usuario());
	    return "auth/register";  // correcto, coincide con templates/auth/register.html
	}

	@PostMapping("/registro")
	public String registrarUsuario(
	        @Valid @ModelAttribute("usuario") Usuario usuario,
	        BindingResult bindingResult,
	        Model model) {

	    if (bindingResult.hasErrors()) {
	        // Cambia "register" por "auth/register" para que Thymeleaf encuentre la plantilla
	        return "auth/register";
	    }

	    boolean existeUsuario = usuarioService.existeUsuarioPorUsername(usuario.getUsername());
	    if (existeUsuario) {
	        model.addAttribute("errorUsuarioExiste", "El usuario ya está registrado.");
	        // Igual aquí, debe ser "auth/register"
	        return "auth/register";
	    }

	    usuarioService.registrarUsuarioConRolUser(usuario);

	    return "redirect:/login?registroExitoso";
	}
}