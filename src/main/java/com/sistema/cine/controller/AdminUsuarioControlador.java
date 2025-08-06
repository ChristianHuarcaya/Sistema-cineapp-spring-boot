package com.sistema.cine.controller;

import com.sistema.cine.entidad.Usuario;
import com.sistema.cine.repository.UsuarioRepository;
import com.sistema.cine.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Size;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioControlador {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("")
	public String listarUsuarios(Model model) {
		List<Usuario> usuarios = usuarioRepository.findAll();
		model.addAttribute("usuarios", usuarios);
		return "admin/usuarios";
	}

	
	@GetMapping("/{id}/editar-password")
	public String mostrarFormularioCambioPassword(@PathVariable Long id, Model model) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		if (usuario == null) {
			return "redirect:/admin/usuarios?error=UsuarioNoEncontrado";
		}
		model.addAttribute("usuario", usuario);
		return "admin/cambiar-password";
	}
	
	
	
	
	
	@PostMapping("/{id}/editar-password")
	public String cambiarPassword(@PathVariable Long id,
	                              @RequestParam("passwordNueva") String passwordNueva,
	                              Model model) {

	    Usuario usuario = usuarioRepository.findById(id).orElse(null);
	    if (usuario == null) {
	        return "redirect:/admin/usuarios?error=UsuarioNoEncontrado";
	    }

	    if (passwordNueva == null || passwordNueva.length() < 6) {
	        model.addAttribute("usuario", usuario);
	        model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
	        return "admin/cambiar-password";
	    }

	    usuario.setPassword(passwordEncoder.encode(passwordNueva));
	    usuarioRepository.save(usuario);

	    return "redirect:/admin/usuarios?exito=PasswordActualizada";
	}

	

}
