package com.proyects.booksservice.excepcion;

public class LibroNoActualizado extends RuntimeException {
    public LibroNoActualizado(String message) {
        super(message);
    }
}
