package com.biblioteca.tdd.excepciones;

public class LibroNoEncontradoException extends RuntimeException 
{

    public LibroNoEncontradoException(String message) {
        super(message);
    }

}
