package com.proyects.userservice.services;

import com.proyects.userservice.dto.UsuarioDTO;
import com.proyects.userservice.excepcion.UsuarioNoEncontrado;
import com.proyects.userservice.mapper.UsuarioMapper;
import com.proyects.userservice.model.Usuario;
import com.proyects.userservice.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findUsuarioById(Long id){
        Usuario usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontrado("El usuario con la id " + id + " no existe"));

        return usuarioMapper.toDto(usuarioEntity);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAllUsuarios(){
        List<Usuario> usuarioEntidades = usuarioRepository.findAll();

        return usuarioMapper.toDtoList(usuarioEntidades);
    }

    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO){
        Usuario usuarioEntity = usuarioMapper.toEntity(usuarioDTO);

        Usuario entidadGuardada = usuarioRepository.save(usuarioEntity);

        return usuarioMapper.toDto(entidadGuardada);
    }

    @Transactional
    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO){
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con la id " + id + " no existe"));

        String[] nombres = usuarioDTO.getNombres().trim().split(" ",2);
        String nombre = nombres[0];
        String apellido = nombres.length > 1 ? nombres[1] : "";

        usuarioExistente.setNombre(nombre);
        usuarioExistente.setApellido(apellido);
        usuarioExistente.setEmail(usuarioDTO.getCorreo());
        usuarioExistente.setFechaAlta(usuarioDTO.getRegistro());

        Usuario entidadActualizada = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toDto(entidadActualizada);
    }

    @Transactional
    public void deleteUsuario(Long id){
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con la id " + id + " no existe"));

        usuarioRepository.delete(usuarioExistente);
    }

    @Transactional
    public ResponseEntity<UsuarioDTO> updateEstado(Long id, Boolean estado){
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con la id " + id + " no existe"));

        usuarioExistente.setActivo(estado);

        usuarioRepository.save(usuarioExistente);

        return ResponseEntity.ok(usuarioMapper.toDto(usuarioExistente));
    }
}
