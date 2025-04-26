package com.proyects.loanservice.controller;

import com.proyects.loanservice.dto.LoanRequestDTO;
import com.proyects.loanservice.dto.PrestamoDTO;
import com.proyects.loanservice.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<PrestamoDTO> getAllPrestamos(){
        return prestamoService.findAllPrestamos();
    }

    @GetMapping("/{id}")
    public PrestamoDTO getPrestamoById(@PathVariable Long id){
        return prestamoService.findPrestamoById(id);
    }

    @PostMapping("/create")
    public PrestamoDTO createPrestamo(@RequestBody LoanRequestDTO requestDTO){
        return prestamoService.createPrestamo(requestDTO);
    }

    @PutMapping("/update/{id}")
    public PrestamoDTO updatePrestamo(@PathVariable Long id, @RequestBody PrestamoDTO prestamoDTO){
        return prestamoService.updatePrestamo(id, prestamoDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePrestamo(@PathVariable Long id){
        prestamoService.deletePrestamo(id);
    }

}
