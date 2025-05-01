package com.proyects.userservice.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerHandler {

    @ExceptionHandler(UsuarioNoEncontrado.class)
    public ResponseEntity<ExceptionMessage> manejarUsuarioNoEncontrado(UsuarioNoEncontrado ex){
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Usuario no encontrado");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioNoActualizado.class)
    public ResponseEntity<ExceptionMessage> manejarUsuarioNoActualizado(UsuarioNoActualizado ex){
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Usuario no actualizado");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsuarioNoCreado.class)
    public ResponseEntity<ExceptionMessage> manejarUsuarioNoCreado(UsuarioNoCreado ex){
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Usuario no creado");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsuarioNoEliminado.class)
    public ResponseEntity<ExceptionMessage> manejarUsuarioNoEliminado(UsuarioNoEliminado ex){
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Usuario no eliminado");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
