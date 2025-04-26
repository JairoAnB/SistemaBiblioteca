package com.proyects.loanservice.dto;

import lombok.Data;

@Data
public class LoanRequestDTO {

    private Long usuarioPrestamoId;
    private Long libroPrestamoId;
    private Integer stock;
}
