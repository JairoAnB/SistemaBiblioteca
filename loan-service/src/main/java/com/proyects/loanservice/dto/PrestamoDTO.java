package com.proyects.loanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoDTO {

    private Long prestamoId;
    private Long libroId;
    private String idUsuario;
    private String libroTitulo;
    private String libroAutor;
    private String nombrePrestatario;
    private LocalDate fechaPrestado;
    private LocalDate fechaDevolucion;
}
