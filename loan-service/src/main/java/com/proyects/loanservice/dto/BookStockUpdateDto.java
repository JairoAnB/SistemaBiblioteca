package com.proyects.loanservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookStockUpdateDto {

    private Integer stockDisponible;

    public BookStockUpdateDto(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

}
