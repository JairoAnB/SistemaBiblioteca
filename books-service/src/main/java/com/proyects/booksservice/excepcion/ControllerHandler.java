package com.proyects.booksservice.excepcion;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerHandler {

    @ExceptionHandler(LibroNoEncontrado.class)
    public ResponseEntity<ExceptionMessage> manejarLibroNoEncontrado(LibroNoEncontrado ex){
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Libro no encontrado");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LibroNoActualizado.class)
    public ResponseEntity<ExceptionMessage> manejarLibroNoActualizado(LibroNoActualizado ex){
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Libro no actualizado");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibroNoCreado.class)
    public ResponseEntity<ExceptionMessage> manejarLibroNoCreado(LibroNoCreado ex){
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Libro no creado");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibroNoEliminado.class)
    public ResponseEntity<ExceptionMessage> manejarLibroNoEliminado(LibroNoEliminado ex){
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Libro no eliminado");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
