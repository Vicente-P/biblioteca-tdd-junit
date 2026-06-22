package com.biblioteca.tdd;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.biblioteca.tdd.excepciones.DatosInvalidosException;
import com.biblioteca.tdd.excepciones.LibroDuplicadoException;
import com.biblioteca.tdd.excepciones.LibroNoDisponibleException;
import com.biblioteca.tdd.excepciones.LibroNoEncontradoException;
import com.biblioteca.tdd.excepciones.LibroYaDisponibleException;

import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaTest {

    private Biblioteca biblioteca;

    @BeforeEach
    public void configurarBiblioteca() {
        biblioteca = new Biblioteca();
    }

    private Libro crearYRegistrarLibroDePrueba() {
        Libro libro = new Libro("1234567890", "Colmillo blanco", "Jack London", 1943);
        biblioteca.registrarLibro(libro);
        return libro;
    }

    @Test
    @DisplayName("Registrar un libro válido permite encontrarlo por su ISBN")
    public void registrarLibroValido() {
        String isbn = "1234567890";
        
        Libro libro = new Libro( isbn, "Colmillo blanco", "Jack London", 1943);

        biblioteca.registrarLibro(libro);

        Optional<Libro> libroEncontrado = biblioteca.buscarPorIsbn(isbn);

        assertTrue(libroEncontrado.isPresent());
    }

    @Test
    @DisplayName("Verificar que un libro queda disponible")
    public void libroNuevoDisponible() {
        
        Libro libro = crearYRegistrarLibroDePrueba();

        Optional<Libro> libroEncontrado = biblioteca.buscarPorIsbn(libro.getIsbn());

        assertTrue(libroEncontrado.isPresent());
        assertTrue(libroEncontrado.get().isDisponible());
        
    }

    @Test
    @DisplayName("No permitir que se registre un libro con ISBN Vacío")
    public void noRegistrarLibroConIsbnVacio() {

        assertThrows(DatosInvalidosException.class, () -> {
            biblioteca.registrarLibro(new Libro("", "Colmillo blanco", "Jack London", 1943));
        });

        assertThrows(DatosInvalidosException.class, () -> {
            biblioteca.registrarLibro(new Libro(null, "Colmillo blanco", "Jack London", 1943));
        });
        
    }

    @Test
    @DisplayName("No permitir que se registre un libro con título Vacío")
    public void noRegistrarLibroConTituloVacio() {

        assertThrows(DatosInvalidosException.class, () -> {
            biblioteca.registrarLibro(new Libro("1234567890", "", "Jack London", 1943));
        });
        
        assertThrows(DatosInvalidosException.class, () -> {
            biblioteca.registrarLibro(new Libro("1234567890", null, "Jack London", 1943));
        });
        
    }

    @Test
    @DisplayName("No permitir que se registre un libro con ISBN duplicado")
    public void noRegistrarLibroConIsbnDuplicado() {

        Libro libro = new Libro("1234567890", "Colmillo blanco", "Jack London", 1943);
        biblioteca.registrarLibro(libro);

        assertThrows(LibroDuplicadoException.class, () -> {
            biblioteca.registrarLibro(new Libro("1234567890", "Colmillo blanco", "Jack London", 1943));
        });
        
    }

    @Test
    @DisplayName("Buscar un libro por su ISBN")
    public void buscarLibroPorIsbn() {

        Libro libro = crearYRegistrarLibroDePrueba();

        Optional<Libro> libroEncontrado = biblioteca.buscarPorIsbn(libro.getIsbn());

        assertTrue(libroEncontrado.isPresent());
        
    }

    @Test
    @DisplayName("Buscar un libro inexistente por su ISBN")
    public void buscarLibroInexistentePorIsbn() {

        assertThrows(LibroNoEncontradoException.class, () -> {
            biblioteca.buscarPorIsbn("1234567890");
        });
        
    }

    @Test
    @DisplayName("Buscar un libro por coincidencia parcial del titulo")
    public void buscarLibroPorCoincidenciaParcialDelTitulo() {

        Libro libro = crearYRegistrarLibroDePrueba();

        // registramos un nuevo libro con diferente titulo para verificar que se encuentra solo el libro con coincidencia parcial
        Libro libro2 = new Libro("1234567891", "Habitos Atomicos", "James Clear", 2018);
        biblioteca.registrarLibro(libro2);

        List<Libro> librosEncontrados = biblioteca.buscarPorTitulo("Colmillo");
        
        // verificamos que se encuentra solo el libro con coincidencia parcial
        assertEquals(1, librosEncontrados.size());
        assertEquals(libro.getTitulo(), librosEncontrados.get(0).getTitulo());
        
    }

    @Test
    @DisplayName("Buscar un libro por titulo sin diferenciar mayusculas y minusculas")
    public void buscarLibroPorTituloSinDiferenciarMayusculasYMinusculas() {

        Libro libro = crearYRegistrarLibroDePrueba();

        List<Libro> librosEncontrados = biblioteca.buscarPorTitulo("colmillo");

        assertEquals(1, librosEncontrados.size());
        assertEquals(libro.getTitulo(), librosEncontrados.get(0).getTitulo());
        
    }

    @Test
    @DisplayName("Listar todos los libros disponibles")
    public void listarLibrosDisponibles() {

        Libro libro1 = crearYRegistrarLibroDePrueba();
        Libro libro2 = new Libro("1234567891", "Habitos Atomicos", "James Clear", 2018);
        Libro libro3 = new Libro("1234567892", "El principito", "Antoine de Saint-Exupéry", 1943);
        biblioteca.registrarLibro(libro2);
        biblioteca.registrarLibro(libro3);

        biblioteca.prestarLibro(libro1.getIsbn());


        List<Libro> librosDisponibles = biblioteca.listarDisponibles();

        assertEquals(2, librosDisponibles.size());
    }

    @Test
    @DisplayName("Prestar un libro disponible")
    public void prestarLibroDisponible() {

        Libro libro = crearYRegistrarLibroDePrueba();
        assertTrue(libro.isDisponible());

        biblioteca.prestarLibro(libro.getIsbn());
        // Verificar que el libro queda no disponible después del prestamo
        assertFalse(libro.isDisponible());

    }

    @Test
    @DisplayName("No permitir prestar un libro inexistente")
    public void noPrestarLibroInexistente() {

        assertThrows(LibroNoEncontradoException.class, () -> {
            biblioteca.prestarLibro("1234567890");
        });
    }

    @Test
    @DisplayName("No permitir prestar un libro que ya esta prestado")
    public void noPrestarLibroQueYaEstaPrestado() {

        Libro libro = crearYRegistrarLibroDePrueba();

        biblioteca.prestarLibro(libro.getIsbn());

        assertThrows(LibroNoDisponibleException.class, () -> {
            biblioteca.prestarLibro(libro.getIsbn());
        });
    }

    @Test
    @DisplayName("Devolver un libro prestado")
    public void devolverLibroPrestado() {

        Libro libro = crearYRegistrarLibroDePrueba();
        // Verificar que el libro está disponible antes de prestarlo
        assertTrue(libro.isDisponible());

        biblioteca.prestarLibro(libro.getIsbn());
        // Verificar que el libro queda no disponible después del prestamo
        assertFalse(libro.isDisponible());

        biblioteca.devolverLibro(libro.getIsbn());
        // Verificar que el libro queda disponible después de la devolución
        assertTrue(libro.isDisponible());
    }

    @Test
    @DisplayName("No permitir devolver un libro inexistente")
    public void noDevolverLibroInexistente() {

        assertThrows(LibroNoEncontradoException.class, () -> {
            biblioteca.devolverLibro("1234567890");
        });
    }

    @Test
    @DisplayName("No permitar devolver un libro que ya estaba disponible")
    public void noDevolverLibroQueYaEstaDisponible() {

        Libro libro = crearYRegistrarLibroDePrueba();

        assertThrows(LibroYaDisponibleException.class, () -> {
            biblioteca.devolverLibro(libro.getIsbn());
        });
    }
}