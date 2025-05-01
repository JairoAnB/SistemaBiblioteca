package com.proyects.loanservice.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanRequestDTO {

    private Long usuarioPrestamoId;
    private Long libroPrestamoId;
    private String libroTitulo;
    private String nombre;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean estado;
}
