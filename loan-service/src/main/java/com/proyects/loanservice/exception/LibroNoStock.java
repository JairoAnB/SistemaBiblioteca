package com.proyects.loanservice.exception;

public class LibroNoStock extends RuntimeException {
    public LibroNoStock(String message) {
        super(message);
    }
}
