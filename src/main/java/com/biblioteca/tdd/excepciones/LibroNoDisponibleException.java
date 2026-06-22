package com.biblioteca.tdd.excepciones;

public class LibroNoDisponibleException extends RuntimeException {

    public LibroNoDisponibleException(String message) {
        super(message);
    }

}
