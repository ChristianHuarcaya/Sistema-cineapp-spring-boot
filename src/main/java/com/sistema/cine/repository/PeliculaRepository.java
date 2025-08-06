package com.sistema.cine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.cine.entidad.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Integer>{

}
