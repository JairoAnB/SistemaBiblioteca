package com.proyects.loanservice.controller;

import com.proyects.loanservice.dto.BookDTO;
import com.proyects.loanservice.dto.LoanRequestDTO;
import com.proyects.loanservice.dto.PrestamoDTO;
import com.proyects.loanservice.dto.UserDTO;
import com.proyects.loanservice.service.BibliotecaClient;
import com.proyects.loanservice.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping
public class PrestamoWebController {

    private final PrestamoService prestamoService;
    private final RestTemplate restTemplate;
    private final BibliotecaClient bibliotecaClient;

    @Autowired
    public PrestamoWebController(PrestamoService prestamoService, RestTemplate restTemplate, BibliotecaClient bibliotecaClient) {
        this.prestamoService = prestamoService;
        this.restTemplate = restTemplate;
        this.bibliotecaClient = bibliotecaClient;
    }

    @GetMapping("/prestamos")
    public String getAllPrestamos(Model model) {
        List<PrestamoDTO> prestamos = prestamoService.findAllPrestamos();
        List<UserDTO> usuarios = bibliotecaClient.obtenerUsuarios();
        List<BookDTO> libros = bibliotecaClient.obtenerLibros();

        model.addAttribute("prestamos", prestamos);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("libros", libros);
        return "prestamos";  // Nombre de la vista (prestamos.html)
    }

    @PostMapping("/loan/create")
    public String createLoan(LoanRequestDTO loanRequestDTO) {
        try{
            prestamoService.createPrestamo(loanRequestDTO);
            System.out.println("Prestamo creado" + loanRequestDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/prestamos";
    }

}
