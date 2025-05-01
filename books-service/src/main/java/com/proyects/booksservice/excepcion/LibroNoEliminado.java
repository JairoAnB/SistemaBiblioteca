package com.proyects.booksservice.excepcion;

public class LibroNoEliminado extends RuntimeException {
    public LibroNoEliminado(String message) {
        super(message);
    }
}
