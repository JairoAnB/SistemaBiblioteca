package com.proyects.loanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoDTO {

    private Long prestamoId;
    private Long libroId;
    private String idUsuario;
    private String libroPrecio;
    private String fecha;
}
