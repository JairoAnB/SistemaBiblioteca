package com.proyects.loanservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerHandler {




    @ExceptionHandler(PrestamoNoEncontrado.class)
    public ResponseEntity<ExceptionMessage> handlePrestamoNoEncontrado(PrestamoNoEncontrado ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Prestamo no encontrado");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PrestamoNoCreado.class)
    public ResponseEntity<ExceptionMessage> handlePrestamoNoCreado(PrestamoNoCreado ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Prestamo no creado");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PrestamoNoActualizado.class)
    public ResponseEntity<ExceptionMessage> handlePrestamoNoActualizado(PrestamoNoActualizado ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Prestamo no actualizado");
        return new ResponseEntity<>(error, HttpStatus.NOT_MODIFIED);
    }
    @ExceptionHandler(PrestamoNoEliminado.class)
    public ResponseEntity<ExceptionMessage> handlePrestamoNoEliminado(PrestamoNoEliminado ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Prestamo no eliminado");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LibroNoCreado.class)
    public ResponseEntity<ExceptionMessage> handleLibroNoCreado(LibroNoCreado ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Libro no creado, revisa la solicitud");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibroNoExiste.class)
    public ResponseEntity<ExceptionMessage> handleLibroNoActualizado(LibroNoExiste ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Libro no actualizado, revisa la solicitud");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibroNoStock.class)
    public ResponseEntity<ExceptionMessage> handleLibroNoStock(LibroNoStock ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Libro no disponible, revisa el stock");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoPrestamosRestantes.class)
    public ResponseEntity<ExceptionMessage> handleNoPrestamosRestantes(NoPrestamosRestantes ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Prestamo rechazado");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsuarioNoCreado.class)
    public ResponseEntity<ExceptionMessage> handleUsuarioNoCreado(UsuarioNoCreado ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "El usuario no fue creado");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsuarioNoEncontrado.class)
    public ResponseEntity<ExceptionMessage> handleUsuarioNoEncontrado(UsuarioNoEncontrado ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "El usuario no fue encontrado");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleGeneralException(Exception ex) {
        ExceptionMessage error = new ExceptionMessage(ex.getMessage(), "Error interno del servidor, por favor revisa si los servicios estan activos");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
