package com.proyects.userservice.excepcion;

public class UsuarioNoCreado extends RuntimeException {
    public UsuarioNoCreado(String message) {
        super(message);
    }
}
