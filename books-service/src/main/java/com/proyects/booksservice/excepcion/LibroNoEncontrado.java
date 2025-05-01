package com.proyects.booksservice.excepcion;

public class LibroNoEncontrado extends RuntimeException {
    public LibroNoEncontrado(String message) {
        super(message);
    }
}
