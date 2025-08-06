package com.sistema.cine.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sistema.cine.excepciones.AlmacenExcepcion;
import com.sistema.cine.excepciones.FileNotFoundException;

import jakarta.annotation.PostConstruct;

@Service
public class AlmacenServicioImpl implements AlmacenServicio {

	@Value("${storage.location}")
	private String storageLocation;
    
	// este sirve para iniciar que todo se va ejecutar cada vez que 
	@PostConstruct
	@Override
	public void iniciarAlmacenDeArchivo() {
		try {
			Files.createDirectories(Paths.get(storageLocation));
		} catch (IOException causa) {
			throw new AlmacenExcepcion("Error al inicializar la ubicación del almacén de archivos", causa);
		}
	}

	@Override
	public String almacenarArchivo(MultipartFile archivo) {
		String nombreArchivo = archivo.getOriginalFilename();

		if (archivo.isEmpty()) {
			throw new AlmacenExcepcion("No se puede almacenar un archivo vacío");
		}

		try (InputStream inputStream = archivo.getInputStream()) {
			Files.copy(inputStream, Paths.get(storageLocation).resolve(nombreArchivo),
					StandardCopyOption.REPLACE_EXISTING);
			return nombreArchivo;
		} catch (IOException causa) {
			throw new AlmacenExcepcion("Error al almacenar el archivo " + nombreArchivo, causa);
		}
	}

	@Override
	public Path cargarArchivo(String nombreArchivo) {
		return Paths.get(storageLocation).resolve(nombreArchivo);
	}

	@Override
	public Resource cargarComoRecurso(String nombreArchivo) {
		try {
			Path archivo = cargarArchivo(nombreArchivo);
			Resource recurso = new UrlResource(archivo.toUri());

			if (recurso.exists() && recurso.isReadable()) {
				return recurso;
			} else {
				throw new FileNotFoundException("No se pudo encontrar el archivo " + nombreArchivo);
			}
		} catch (MalformedURLException causa) {
			throw new FileNotFoundException("No se puede acceder al archivo " + nombreArchivo, causa);
		}
	}

	@Override
	public void eliminarArchivo(String nombreArchivo) {
		Path archivo = cargarArchivo(nombreArchivo);
		try {
			FileSystemUtils.deleteRecursively(archivo);
		} catch (Exception causa) {
			System.out.println("Error al eliminar archivo: " + causa.getMessage());
		}
	}
}
