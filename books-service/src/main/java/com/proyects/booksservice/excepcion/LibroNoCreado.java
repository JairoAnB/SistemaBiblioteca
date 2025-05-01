package com.proyects.booksservice.excepcion;

public class LibroNoCreado extends RuntimeException {
    public LibroNoCreado(String message) {
        super(message);
    }
}
