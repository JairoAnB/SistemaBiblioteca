package com.proyects.loanservice.mapper;


import com.proyects.loanservice.dto.LoanRequestDTO;
import com.proyects.loanservice.dto.PrestamoDTO;
import com.proyects.loanservice.model.Prestamo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    //Mapeo entre Prestamo y PrestamoDTO
    @Mappings({
            @Mapping(source = "id", target = "prestamoId" ),
            @Mapping(source = "usuarioId", target = "idUsuario" ),
            @Mapping(source = "monto", target = "libroPrecio" ),
            @Mapping(source = "fechaPrestamo", target = "fechaPrestado" ),
            @Mapping(source = "libroTitulo", target = "libroTitulo" ),
            @Mapping(source = "libroAutor", target = "libroAutor" ),
            @Mapping(source = "usuarioNombre", target = "nombrePrestatario" ),
            @Mapping(source = "fechaDevolucion", target = "fechaDevolucion" ),
    })
    PrestamoDTO toDto(Prestamo prestamo);

    @Mappings({
            @Mapping(source = "prestamoId", target = "id" ),
            @Mapping(source = "idUsuario", target = "usuarioId" ),
            @Mapping(source = "libroPrecio", target = "monto" ),
            @Mapping(source = "fechaPrestado", target = "fechaPrestamo" ),
            @Mapping(source = "libroTitulo", target = "libroTitulo" ),
            @Mapping(source = "libroAutor", target = "libroAutor" ),
            @Mapping(source = "nombrePrestatario", target = "usuarioNombre" ),
            @Mapping(source = "fechaDevolucion", target = "fechaDevolucion" ),
    })

    Prestamo toEntity(PrestamoDTO prestamoDTO);

    List<PrestamoDTO> toDtoList(List<Prestamo> prestamos);
    List<Prestamo> toEntityList(List<PrestamoDTO> prestamoDTOs);

    //Mapeo entre Prestamo y LoanRequestDTO

    @Mappings({
            @Mapping(source = "usuarioId", target = "usuarioPrestamoId"),
            @Mapping(source = "libroId", target = "libroPrestamoId"),
            @Mapping(source = "libroTitulo", target = "libroTitulo"),
            @Mapping(source = "usuarioNombre", target = "nombre"),
            @Mapping(source = "monto", target = "libroPrecio"),
            @Mapping(source = "fechaPrestamo", target = "fechaPrestamo"),
            @Mapping(source = "fechaDevolucion", target = "fechaDevolucion"),
    })
    LoanRequestDTO toLoanRequestDTO(Prestamo prestamo);

    @Mappings({
            @Mapping(source = "usuarioPrestamoId", target = "usuarioId"),
            @Mapping(source = "libroPrestamoId", target = "libroId"),
            @Mapping(source = "libroTitulo", target = "libroTitulo"),
            @Mapping(source = "nombre", target = "usuarioNombre"),
            @Mapping(source = "libroPrecio", target = "monto"),
            @Mapping(source = "fechaPrestamo", target = "fechaPrestamo"),
            @Mapping(source = "fechaDevolucion", target = "fechaDevolucion"),
    })
    Prestamo toPrestamoEntity(LoanRequestDTO loanRequestDTO);

    List<LoanRequestDTO> toLoanRequestDTOList(List<Prestamo> prestamos);
    List<Prestamo> toPrestamoList(List<LoanRequestDTO> loanRequestDTOs);
}
