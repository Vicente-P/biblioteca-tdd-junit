package com.biblioteca.tdd.excepciones;

public class LibroYaDisponibleException extends RuntimeException {

    public LibroYaDisponibleException(String message) {
        super(message);
    }

}
