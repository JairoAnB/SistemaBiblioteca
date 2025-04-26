package com.proyects.booksservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long libroId;
    private String libroTitulo;
    private String autor;
    private String isbn;
    private Integer stock;

}
