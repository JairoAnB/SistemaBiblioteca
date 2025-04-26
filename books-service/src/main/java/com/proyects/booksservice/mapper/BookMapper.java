package com.proyects.booksservice.mapper;

import com.proyects.booksservice.dto.BookDTO;
import com.proyects.booksservice.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mappings({
            @Mapping(source = "id", target = "libroId"), //MAPEO DE ID -> bookId
            @Mapping(source = "titulo", target = "libroTitulo"),
            @Mapping(source = "autorName", target = "autor"),
            //si alguno tiene el mismo nombre se mapea automaticamente
    })

    BookDTO toDto(Book book);

    @Mappings({
            //LO MISMO PERO A LA INVERSA
            @Mapping(source = "libroId", target = "id"),
            @Mapping(source = "libroTitulo", target = "titulo"),
            @Mapping(source = "autor", target = "autorName"),
            //si alguno tiene el mismo nombre se mapea automaticamente
    })

    Book toEntity(BookDTO bookDTO);

    //Tambien se puede generar automaticamente los mismos metodos para las listas

    List<BookDTO> toDtoList(List<Book> books);
    List<Book> toEntityList(List<BookDTO> bookDTOs);
}
