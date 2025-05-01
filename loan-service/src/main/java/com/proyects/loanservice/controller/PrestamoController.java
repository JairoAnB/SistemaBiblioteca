package com.proyects.loanservice.controller;

import com.proyects.loanservice.dto.LoanRequestDTO;
import com.proyects.loanservice.dto.PrestamoDTO;
import com.proyects.loanservice.dto.UserDTO;
import com.proyects.loanservice.exception.PrestamoNoCreado;
import com.proyects.loanservice.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoDTO>>  getAllPrestamos(){

        List<PrestamoDTO> prestamos = prestamoService.findAllPrestamos();
        if(prestamos.isEmpty()){
            return ResponseEntity.noContent().build(); //SI NO HAY DATOS SE RETORNA NO CONTENT 204
        }
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDTO> getPrestamoById(@PathVariable Long id){

        PrestamoDTO dto = prestamoService.findPrestamoById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPrestamo(@RequestBody LoanRequestDTO requestDTO){

        prestamoService.createPrestamo(requestDTO);
        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("El prestamo fue creado correctamente.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePrestamo(@PathVariable Long id, @RequestBody PrestamoDTO prestamoDTO){

        PrestamoDTO dto = prestamoService.updatePrestamo(id, prestamoDTO);

        return ResponseEntity
                .status(200)
                .body("El prestamo fue actualizado correctamente.");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePrestamo(@PathVariable Long id){
        prestamoService.refundPrestamo(id);
        return ResponseEntity.ok("El prestamo fue eliminado correctamente.");
    }


}
