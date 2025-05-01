package com.proyects.userservice.controller;


import com.proyects.userservice.dto.UsuarioDTO;
import com.proyects.userservice.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class ApiController {

    private final UsuarioService usuarioService;

    @Autowired
    public ApiController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.findAllUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build(); // SI NO HAY DATOS SE RETORNA NO CONTENT 204
        }
        return ResponseEntity.ok(usuarios); // SI HAY DATOS SE RETORNA OK 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {

        UsuarioDTO dto = usuarioService.findUsuarioById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try{
            UsuarioDTO usuario = usuarioService.createUsuario(usuarioDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("El usuario fue creado correctamente.");

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El usuario no fue creado correctamente.");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {

        try {
            UsuarioDTO usuario = usuarioService.updateUsuario(id, usuarioDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("El usuario fue actualizado correctamente.");
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El usuario no fue actualizado correctamente, por favor revisa los datos ingresados.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok("El usuario con la id " + id + " fue eliminado correctamente.");
    }

    @PutMapping("/updateEstado/{id}/{estado}")
    public void updateEstado(@PathVariable Long id, @PathVariable Boolean estado) {
       usuarioService.updateEstado(id, estado);
    }

}
