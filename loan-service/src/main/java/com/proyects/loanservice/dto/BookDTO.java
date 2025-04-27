package com.proyects.loanservice.dto;

import lombok.Data;

@Data
public class BookDTO {

    private Long libroId;
    private String libroTitulo;
    private String autor;
    private String isbn;
    private Integer stock;

}
