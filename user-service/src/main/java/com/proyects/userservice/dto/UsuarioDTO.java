package com.proyects.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long identificador;
    private String nombres;
    private String correo;
    private LocalDate registro;

}
