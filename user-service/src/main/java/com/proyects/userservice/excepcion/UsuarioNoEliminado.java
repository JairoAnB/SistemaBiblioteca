package com.proyects.userservice.excepcion;

public class UsuarioNoEliminado extends RuntimeException {
    public UsuarioNoEliminado(String message) {
        super(message);
    }
}
