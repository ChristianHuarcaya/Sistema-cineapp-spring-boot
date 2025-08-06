package com.sistema.cine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorControlador {
	
	@GetMapping("/403")
	public String accesoDenegago(){
		return "403";
	}

}
