package com.proyects.userservice.excepcion;

public class UsuarioNoEncontrado extends RuntimeException {
    public UsuarioNoEncontrado(String message) {
        super(message);
    }
}
