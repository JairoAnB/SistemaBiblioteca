package com.proyects.loanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private Long identificador;
    private String nombres;
    private String correo;
    private String registro;
    private boolean estado;
}
