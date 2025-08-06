package com.sistema.cine.controller;

import com.sistema.cine.entidad.Genero;
import com.sistema.cine.entidad.Pelicula;
import com.sistema.cine.repository.GeneroRepository;
import com.sistema.cine.repository.PeliculaRepository;
import com.sistema.cine.service.AlmacenServicioImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminControlador {

	@Autowired
	private PeliculaRepository peliculaRepository;

	@Autowired
	private GeneroRepository generoRepository;

	@Autowired
	private AlmacenServicioImpl servicio;

	// Método privado para cargar géneros ordenados
	private List<Genero> cargarGeneros() {
		return generoRepository.findAll(Sort.by("titulo"));
	}

	@GetMapping("")
	public String verPaginaDeInicio(@PageableDefault(sort = "titulo", size = 5) Pageable pageable, Model model) {
		Page<Pelicula> peliculas = peliculaRepository.findAll(pageable);
		model.addAttribute("peliculas", peliculas);
		return "admin/index";
	}

	@GetMapping("/peliculas/nuevo")
	public String mostrarFormularioDeNuevaPelicula(Model model) {
		model.addAttribute("pelicula", new Pelicula());
		model.addAttribute("generos", cargarGeneros());
		return "admin/nuevo-pelicula";
	} 

	@PostMapping("/peliculas/nuevo")
	public String registrarPelicula(@Validated Pelicula pelicula, BindingResult bindingResult, Model model) {
		if (pelicula.getPortada() == null || pelicula.getPortada().isEmpty()) {
			bindingResult.rejectValue("portada", "MultipartNotEmpty", "La portada es obligatoria");
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("generos", cargarGeneros());
			return "admin/nuevo-pelicula";
		}

		String rutaPortada = servicio.almacenarArchivo(pelicula.getPortada());
		pelicula.setRutaPortada(rutaPortada);

		peliculaRepository.save(pelicula);

		return "redirect:/admin";
	}

	@GetMapping("/peliculas/{id}/editar")
	public String mostrarFormularioDeEditarPelicula(@PathVariable Integer id, Model model) {
		Optional<Pelicula> optionalPelicula = peliculaRepository.findById(id);
		if (optionalPelicula.isEmpty()) {
			return "redirect:/admin?error=PelículaNoEncontrada";
		}
		model.addAttribute("pelicula", optionalPelicula.get());
		model.addAttribute("generos", cargarGeneros());
		return "admin/editar-pelicula";
	}

	@PostMapping("/peliculas/{id}/editar")
	public String actualizarPelicula(@PathVariable Integer id, @Validated Pelicula pelicula,
			BindingResult bindingResult, Model model) {
		Optional<Pelicula> optionalPeliculaDB = peliculaRepository.findById(id);
		if (optionalPeliculaDB.isEmpty()) {
			return "redirect:/admin?error=PelículaNoEncontrada";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("generos", cargarGeneros());
			return "admin/editar-pelicula";
		}

		Pelicula peliculaDB = optionalPeliculaDB.get();

		peliculaDB.setTitulo(pelicula.getTitulo());
		peliculaDB.setSinopsis(pelicula.getSinopsis());
		peliculaDB.setFechaEstreno(pelicula.getFechaEstreno());
		peliculaDB.setYoutubeTrailerId(pelicula.getYoutubeTrailerId());
		peliculaDB.setGeneros(pelicula.getGeneros());

		if (pelicula.getPortada() != null && !pelicula.getPortada().isEmpty()) {
			// Eliminar archivo solo si ruta no es null o vacío
			if (peliculaDB.getRutaPortada() != null && !peliculaDB.getRutaPortada().isEmpty()) {
				servicio.eliminarArchivo(peliculaDB.getRutaPortada());
			}
			String rutaPortada = servicio.almacenarArchivo(pelicula.getPortada());
			peliculaDB.setRutaPortada(rutaPortada);
		}

		peliculaRepository.save(peliculaDB);
		return "redirect:/admin";
	}

	@PostMapping("/peliculas/{id}/eliminar")
	public String eliminarPelicula(@PathVariable Integer id) {
		Optional<Pelicula> optionalPelicula = peliculaRepository.findById(id);
		if (optionalPelicula.isPresent()) {
			Pelicula pelicula = optionalPelicula.get();
			peliculaRepository.delete(pelicula);
			if (pelicula.getRutaPortada() != null && !pelicula.getRutaPortada().isEmpty()) {
				servicio.eliminarArchivo(pelicula.getRutaPortada());
			}
		}
		return "redirect:/admin";
	}

}
