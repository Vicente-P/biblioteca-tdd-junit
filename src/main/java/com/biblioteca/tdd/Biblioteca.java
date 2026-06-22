package com.biblioteca.tdd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.biblioteca.tdd.excepciones.DatosInvalidosException;
import com.biblioteca.tdd.excepciones.LibroDuplicadoException;
import com.biblioteca.tdd.excepciones.LibroNoDisponibleException;
import com.biblioteca.tdd.excepciones.LibroNoEncontradoException;
import com.biblioteca.tdd.excepciones.LibroYaDisponibleException;

public class Biblioteca {

    private Map<String, Libro> libros  = new HashMap<>();

    public void registrarLibro(Libro libro) {

        if (libro.getIsbn() == null || libro.getIsbn().isEmpty()) {
            throw new DatosInvalidosException("El ISBN no puede ser vacío");
        }
        if (libro.getTitulo() == null || libro.getTitulo().isEmpty()) {
            throw new DatosInvalidosException("El título no puede ser vacío");
        }

        if (libros.containsKey(libro.getIsbn())) {
            throw new LibroDuplicadoException("El libro con ISBN " + libro.getIsbn() + " ya existe");
        }

        libros.put(libro.getIsbn(), libro);
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        if (!libros.containsKey(isbn)) {
            throw new LibroNoEncontradoException("El libro con ISBN " + isbn + " no existe");
        }
        return Optional.ofNullable(libros.get(isbn));
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return libros.values().stream()
            .filter(libro -> libro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Libro> listarDisponibles() {
        return libros.values().stream()
            .filter(libro -> libro.isDisponible())
            .collect(Collectors.toList());
    }

    public void prestarLibro(String isbn) {
        if (!libros.containsKey(isbn)) {
            throw new LibroNoEncontradoException("El libro con ISBN " + isbn + " no existe");
        }
        if (!libros.get(isbn).isDisponible()) {
            throw new LibroNoDisponibleException("El libro con ISBN " + isbn + " no está disponible");
        }
        libros.get(isbn).setDisponible(false);
    }

    public void devolverLibro(String isbn) {
        if (!libros.containsKey(isbn)) {
            throw new LibroNoEncontradoException("El libro con ISBN " + isbn + " no existe");
        }
        if (libros.get(isbn).isDisponible()) {
            throw new LibroYaDisponibleException("El libro con ISBN " + isbn + " ya está disponible");
        }
        libros.get(isbn).setDisponible(true);
    }



}
