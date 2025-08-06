package com.sistema.cine.controller;

import com.sistema.cine.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginControlador {

	@GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // apunta a templates/login.html
    }

}

