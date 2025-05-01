package com.proyects.userservice.mapper;


import com.proyects.userservice.dto.UsuarioDTO;
import com.proyects.userservice.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mappings({
            @Mapping(source = "id", target = "identificador"),
            @Mapping(target = "nombres", expression = "java(usuario.getNombre() + \" \" + usuario.getApellido())"),
            @Mapping(source = "email", target= "correo"),
            @Mapping(source = "fechaAlta", target = "registro"),
            @Mapping(source = "activo", target = "estado"),

    })
    UsuarioDTO toDto(Usuario usuario);

    @Mappings({
            @Mapping(source = "identificador", target = "id"),
            // Manejo de nombres con posibles múltiples palabras
            @Mapping(target = "nombre", expression = "java(usuarioDTO.getNombres().split(\" \")[0])"),
            @Mapping(target = "apellido", expression = "java(usuarioDTO.getNombres().substring(usuarioDTO.getNombres().indexOf(\" \") + 1))"),
            @Mapping(source = "correo", target= "email"),
            @Mapping(source = "registro", target = "fechaAlta"),
            @Mapping(source = "estado", target = "activo"),

    })
    Usuario toEntity(UsuarioDTO usuarioDTO);

    List<UsuarioDTO> toDtoList(List<Usuario> usuarios);
    List<Usuario> toEntityList(List<UsuarioDTO> usuarioDTOs);
}
