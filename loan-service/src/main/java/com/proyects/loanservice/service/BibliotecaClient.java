package com.proyects.loanservice.service;

import com.proyects.loanservice.dto.BookDTO;
import com.proyects.loanservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BibliotecaClient {

    private final RestTemplate restTemplate;

    @Autowired
    public BibliotecaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    String urlUsuarios = "http://localhost:8082/api/usuarios";
    String urlLibros = "http://localhost:8081/api/books";
    String urlLibrosStock = "http://localhost:8081/api/books/updateStock";

    public List<BookDTO> obtenerLibros(){
        try{
            BookDTO[] libros = restTemplate.getForObject(urlLibros, BookDTO[].class);
            return libros != null ? Arrays.asList(libros) : Collections.emptyList();
        } catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<UserDTO> obtenerUsuarios(){
        try{
            UserDTO[] usuarios = restTemplate.getForObject(urlUsuarios, UserDTO[].class);
            return usuarios != null ? Arrays.asList(usuarios) : Collections.emptyList();
        } catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
