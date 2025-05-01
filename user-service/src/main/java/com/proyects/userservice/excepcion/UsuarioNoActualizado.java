package com.proyects.userservice.excepcion;

public class UsuarioNoActualizado extends RuntimeException {
    public UsuarioNoActualizado(String message) {
        super(message);
    }
}
