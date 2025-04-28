package com.proyects.loanservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequestDTO {

    private Long usuarioPrestamoId;
    private Long libroPrestamoId;
    private Integer stock;
    private String libroTitulo;
    private String Nombre;
    private Integer libroPrecio;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
}
