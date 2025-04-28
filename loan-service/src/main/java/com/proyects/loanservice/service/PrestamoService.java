package com.proyects.loanservice.service;

import com.proyects.loanservice.dto.*;
import com.proyects.loanservice.mapper.PrestamoMapper;
import com.proyects.loanservice.model.Prestamo;
import com.proyects.loanservice.repository.PrestamoRepository;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final RestTemplate restTemplate;
    private final PrestamoRepository prestamoRepository;
    private final PrestamoMapper prestamoMapper;
    private final String user_service_url = "http://localhost:8082/api/usuarios/";
    private final String book_service_url = "http://localhost:8081/api/books/";
    private final String book_service_url_stock = "http://localhost:8081/api/books/updateStock/";

    @Autowired
    public PrestamoService(RestTemplate restTemplate, PrestamoRepository prestamoRepository, PrestamoMapper prestamoMapper) {
        this.restTemplate = restTemplate;
        this.prestamoRepository = prestamoRepository;
        this.prestamoMapper = prestamoMapper;
    }
    public UserDTO validateUser(Long usuarioPrestamoId) {
        // Realiza un GET a la API de usuarios usando RestTemplate
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(user_service_url + usuarioPrestamoId, UserDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            System.out.println("Usuario válido: " + response.getBody().getNombres());
            return response.getBody(); // <- Aquí retornas el usuario completo
        } else {
            throw new RuntimeException("Usuario no válido o no encontrado");
        }
    }

    public BookDTO validateBook(Long libroId) {
        try {
            // Obtener la información del libro
            ResponseEntity<BookDTO> bookResponse = restTemplate.getForEntity(
                    book_service_url + libroId,
                    BookDTO.class
            );

            if (!bookResponse.getStatusCode().is2xxSuccessful() || bookResponse.getBody() == null) {
                throw new RuntimeException("El libro con la id " + libroId + " no existe");
            }

            BookDTO bookDTO = bookResponse.getBody();

            // Validar el stock
            if (bookDTO.getStock() <= 0) {
                throw new RuntimeException("El libro con la id " + libroId + " no tiene stock");
            }

            // Actualizar el stock del libro
            BookStockUpdateDto updateDto = new BookStockUpdateDto(bookDTO.getStock() - 1);

            restTemplate.exchange(
                    book_service_url_stock + libroId + "/" + (bookDTO.getStock() - 1),
                    HttpMethod.PUT,
                    new HttpEntity<>(updateDto),
                    BookStockUpdateDto.class
            );

            System.out.println("Stock actualizado correctamente");

            return bookDTO; //

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al validar el libro: " + e.getMessage());
        }
    }



    @Transactional(readOnly = true)
    public PrestamoDTO findPrestamoById(Long id){
        Prestamo prestamoEntity = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El prestamo con la id " + id + " no existe"));
        return prestamoMapper.toDto(prestamoEntity);
    }

    @Transactional(readOnly = true)
    public List<PrestamoDTO> findAllPrestamos(){
        List<Prestamo> prestamoEntidades = prestamoRepository.findAll();

        return prestamoMapper.toDtoList(prestamoEntidades);
    }

    //Este metodo se encarga de crear un prestamo

    @Transactional
    public PrestamoDTO createPrestamo(LoanRequestDTO request){
        //Validar el usuario y el libro
        UserDTO userDTO =  validateUser(request.getUsuarioPrestamoId());
        BookDTO bookDTO = validateBook(request.getLibroPrestamoId());

        //Si existe se sigue con el proceso
        Prestamo prestamoEntity = prestamoMapper.toPrestamoEntity(request);
        prestamoEntity.setFechaPrestamo(LocalDate.now());
        prestamoEntity.setFechaDevolucion(request.getFechaDevolucion());
        prestamoEntity.setLibroTitulo(bookDTO.getLibroTitulo());
        prestamoEntity.setLibroAutor(bookDTO.getAutor());
        prestamoEntity.setUsuarioNombre(userDTO.getNombres());
        //Se guarda el prestamo en la base de datos
        Prestamo prestamoGuardado = prestamoRepository.save(prestamoEntity);

        return prestamoMapper.toDto(prestamoGuardado);
    }

    @Transactional
    public PrestamoDTO updatePrestamo(Long id, PrestamoDTO prestamoDTO){
        Prestamo prestamoExistente = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El prestamo con la id " + id + " no existe"));

        Prestamo temporalInfo = prestamoMapper.toEntity(prestamoDTO);

        prestamoExistente.setUsuarioId(temporalInfo.getUsuarioId());
        prestamoExistente.setLibroId(temporalInfo.getLibroId());
        prestamoExistente.setMonto(temporalInfo.getMonto());
        prestamoExistente.setFechaPrestamo(temporalInfo.getFechaPrestamo());
        prestamoExistente.setFechaDevolucion(temporalInfo.getFechaDevolucion());
        prestamoExistente.setLibroTitulo(temporalInfo.getLibroTitulo());
        prestamoExistente.setLibroAutor(temporalInfo.getLibroAutor());
        prestamoExistente.setUsuarioNombre(temporalInfo.getUsuarioNombre());


        Prestamo prestamoEntity = prestamoRepository.save(prestamoExistente);

        return prestamoMapper.toDto(prestamoEntity);
    }

    @Transactional
    public void deletePrestamo(Long id){
        if(!prestamoRepository.existsById(id)){
            throw new RuntimeException("El prestamo con la id " + id + " no existe");
        }
        prestamoRepository.deleteById(id);
    }

    @Transactional
    public PrestamoDTO refundPrestamo(Long id){
        Prestamo prestamoExistente = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El prestamo con la id " + id + " no existe"));

        int libroId = prestamoExistente.getLibroId().intValue();
        //Actualizar el stock del libro
        ResponseEntity<BookDTO> bookResponse = restTemplate.getForEntity(
                book_service_url + libroId,
                BookDTO.class
        );
        if(!bookResponse.getStatusCode().is2xxSuccessful() || bookResponse.getBody() == null){
            throw new RuntimeException("El libro con la id " + libroId + " no existe");
        }

        BookDTO bookDTO = bookResponse.getBody();

        BookStockUpdateDto updateDto = new BookStockUpdateDto(bookDTO.getStock() + 1);

        ResponseEntity<BookStockUpdateDto> updateDtoResponse = restTemplate.exchange
                        (book_service_url_stock + libroId + "/" + (bookDTO.getStock() + 1),
                                HttpMethod.PUT, new HttpEntity<>(updateDto)
                                , BookStockUpdateDto.class);
        deletePrestamo(id);
        System.out.println("Libro devuelto correctamente");

        return prestamoMapper.toDto(prestamoExistente);
    }
}
