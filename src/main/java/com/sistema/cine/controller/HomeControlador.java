package com.sistema.cine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sistema.cine.entidad.Pelicula;
import com.sistema.cine.repository.PeliculaRepository;

@Controller
@RequestMapping("")
public class HomeControlador {

    @Autowired
    private PeliculaRepository peliculaRepository;

    // Página principal con últimas películas
    @GetMapping("")
    public String verPaginaPrincipal(Model model) {
        List<Pelicula> ultimasPeliculas = peliculaRepository.findAll(
            PageRequest.of(0, 4, Sort.by("fechaEstreno").descending())
        ).getContent();
        model.addAttribute("ultimasPeliculas", ultimasPeliculas);
        return "index"; // archivo: templates/index.html
    }

    // Listado completo de películas con paginación
    @GetMapping("/peliculas")
    public String listarPeliculas(
        @PageableDefault(sort = "fechaEstreno", direction = Sort.Direction.DESC) Pageable pageable,
        Model model
    ) {
        Page<Pelicula> peliculas = peliculaRepository.findAll(pageable);
        model.addAttribute("peliculas", peliculas);
        return "peliculas"; // archivo: templates/peliculas.html
    }

    // Página para ver tráiler (sin login)
    @GetMapping("/peliculas/{id}/trailer")
    public String verTrailer(@PathVariable Integer id, Model model) {
        Pelicula pelicula = peliculaRepository.findById(id).orElse(null);
        if (pelicula == null) return "redirect:/";
        model.addAttribute("pelicula", pelicula);
        return "cliente/trailer"; // archivo: templates/cliente/trailer.html
    }

    // Página para ver película (con login)
    @GetMapping("/peliculas/{id}/ver")
    public String verPelicula(@PathVariable Integer id, Model model) {
        Pelicula pelicula = peliculaRepository.findById(id).orElse(null);
        if (pelicula == null) return "redirect:/";
        model.addAttribute("pelicula", pelicula);
        return "cliente/ver-pelicula"; // archivo: templates/cliente/ver-pelicula.html
    }
}
