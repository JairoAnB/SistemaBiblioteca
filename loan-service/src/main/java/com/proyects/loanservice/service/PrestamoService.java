package com.proyects.loanservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyects.loanservice.dto.*;
import com.proyects.loanservice.exception.*;
import com.proyects.loanservice.mapper.PrestamoMapper;
import com.proyects.loanservice.model.Prestamo;
import com.proyects.loanservice.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private static final String USER_SERVICE_URL = "http://localhost:8082/api/usuarios/";
    private static final String BOOK_SERVICE_URL = "http://localhost:8081/api/books/";
    private static final String BOOK_SERVICE_URL_STOCK = "http://localhost:8081/api/books/updateStock/";

    private final RestTemplate restTemplate;
    private final PrestamoRepository prestamoRepository;
    private final PrestamoMapper prestamoMapper;

    @Autowired
    public PrestamoService(RestTemplate restTemplate, PrestamoRepository prestamoRepository, PrestamoMapper prestamoMapper) {
        this.restTemplate = restTemplate;
        this.prestamoRepository = prestamoRepository;
        this.prestamoMapper = prestamoMapper;
    }

    public UserDTO validateUser(Long usuarioPrestamoId) {
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(USER_SERVICE_URL + usuarioPrestamoId, UserDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new UsuarioNoEncontrado("El usuario con la id " + usuarioPrestamoId + " no existe en la base de datos");
        }
    }

    public BookDTO validateBook(Long libroId) {
        try {
            // Paso 1: Obtener información del libro
            ResponseEntity<BookDTO> bookResponse = restTemplate.getForEntity(BOOK_SERVICE_URL + libroId, BookDTO.class);

            if (!bookResponse.getStatusCode().is2xxSuccessful() || bookResponse.getBody() == null) {
                throw new LibroNoExiste("No se pudo obtener información del libro con id " + libroId);
            }

            BookDTO bookDTO = bookResponse.getBody();

            // Paso 2: Verificar stock
            if (bookDTO.getStock() < 0) {
                throw new LibroNoStock("El libro con la id " + libroId + " no tiene stock disponible");
            }

            // Paso 3: Calcular nuevo stock
            int nuevoStock = bookDTO.getStock() - 1;

            // Paso 4: Actualizar stock - Usando sólo la URL
            String updateUrl = BOOK_SERVICE_URL_STOCK + libroId + "/" + nuevoStock;

            // Opción simple: usar restTemplate.put sin esperar respuesta
            // restTemplate.put(updateUrl, null);

            // Mejor opción: usar exchange y obtener la respuesta
            try {
                ResponseEntity<BookDTO> updateResponse = restTemplate.exchange(
                        updateUrl,
                        HttpMethod.PUT,
                        null,
                        BookDTO.class
                );

                if (updateResponse.getBody() != null) {
                    return updateResponse.getBody(); // Devolvemos el libro con el stock actualizado
                } else {
                    // Actualizamos el stock localmente ya que no obtuvimos el objeto actualizado
                    bookDTO.setStock(nuevoStock);
                    return bookDTO;
                }

            } catch (HttpClientErrorException ex) {
                throw new LibroNoExiste("Error al actualizar el stock: " + ex.getResponseBodyAsString());
            }

        } catch (HttpClientErrorException ex) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ExceptionMessage error = mapper.readValue(ex.getResponseBodyAsString(), ExceptionMessage.class);
                throw new LibroNoExiste(error.getMessage());
            } catch (Exception parseEx) {

                throw new LibroNoExiste("Error desde booksservice: " + ex.getResponseBodyAsString());
            }
        } catch (Exception ex) {
            throw new LibroNoExiste("Error inesperado al validar el libro: " + ex.getMessage());
        }
    }


    @Transactional(readOnly = true)
    public PrestamoDTO findPrestamoById(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new PrestamoNoEncontrado("El prestamo con la id " + id + " no existe"));
        return prestamoMapper.toDto(prestamo);
    }

    @Transactional(readOnly = true)
    public List<PrestamoDTO> findAllPrestamos() {
        return prestamoMapper.toDtoList(prestamoRepository.findAll());
    }

    @Transactional
    public ResponseEntity<PrestamoDTO> createPrestamo(LoanRequestDTO request) {
        UserDTO userDTO = validateUser(request.getUsuarioPrestamoId());

        if (userDTO.isEstado()) {
            throw new NoPrestamosRestantes("El usuario con la id " + request.getUsuarioPrestamoId() + " ya tiene un prestamo activo");
        }

        BookDTO bookDTO = validateBook(request.getLibroPrestamoId());

        Prestamo prestamo = prestamoMapper.toPrestamoEntity(request);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucion(request.getFechaDevolucion());
        prestamo.setLibroTitulo(bookDTO.getLibroTitulo());
        prestamo.setLibroAutor(bookDTO.getAutor());
        prestamo.setUsuarioNombre(userDTO.getNombres());
        prestamo.setLibroId(request.getLibroPrestamoId());
        prestamo.setUsuarioId(request.getUsuarioPrestamoId());

        Prestamo guardado = prestamoRepository.save(prestamo);

        // Actualizar estado del usuario a "con préstamo activo"
        restTemplate.exchange(
                USER_SERVICE_URL + "updateEstado/" + userDTO.getIdentificador() + "/true",
                HttpMethod.PUT,
                new HttpEntity<>(userDTO),
                UserDTO.class
        );

        return ResponseEntity
                .ok()
                .body(prestamoMapper.toDto(guardado));
    }

    @Transactional
    public PrestamoDTO updatePrestamo(Long id, PrestamoDTO prestamoDTO) {
        Prestamo existente = prestamoRepository.findById(id)
                .orElseThrow(() -> new PrestamoNoEncontrado("El prestamo con la id " + id + " no existe"));

        Prestamo temporal = prestamoMapper.toEntity(prestamoDTO);

        existente.setUsuarioId(temporal.getUsuarioId());
        existente.setLibroId(temporal.getLibroId());
        existente.setMonto(temporal.getMonto());
        existente.setFechaPrestamo(temporal.getFechaPrestamo());
        existente.setFechaDevolucion(temporal.getFechaDevolucion());
        existente.setLibroTitulo(temporal.getLibroTitulo());
        existente.setLibroAutor(temporal.getLibroAutor());
        existente.setUsuarioNombre(temporal.getUsuarioNombre());

        return prestamoMapper.toDto(prestamoRepository.save(existente));
    }

    @Transactional
    public void deletePrestamo(Long id) {
        if (!prestamoRepository.existsById(id)) {
            throw new PrestamoNoEncontrado("El prestamo con la id " + id + " no existe");
        }
        prestamoRepository.deleteById(id);
    }

    @Transactional
    public PrestamoDTO refundPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new PrestamoNoEncontrado("El prestamo con la id " + id + " no existe"));

        Long libroId = prestamo.getLibroId();
        Long usuarioId = prestamo.getUsuarioId();

        // Obtener libro
        ResponseEntity<BookDTO> bookResponse = restTemplate.getForEntity(BOOK_SERVICE_URL + libroId, BookDTO.class);
        if (!bookResponse.getStatusCode().is2xxSuccessful() || bookResponse.getBody() == null) {
            throw new LibroNoExiste("El libro con la id " + libroId + " no existe");
        }

        BookDTO bookDTO = bookResponse.getBody();

        // Actualizar stock
        BookStockUpdateDto updateDto = new BookStockUpdateDto(bookDTO.getStock() + 1);
        restTemplate.exchange(
                BOOK_SERVICE_URL_STOCK + libroId + "/" + (bookDTO.getStock() + 1),
                HttpMethod.PUT,
                new HttpEntity<>(updateDto),
                BookStockUpdateDto.class
        );

        // Actualizar estado del usuario
        restTemplate.exchange(
                USER_SERVICE_URL + "updateEstado/" + usuarioId + "/false",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                EstadoPrestamoDTO.class
        );

        // Eliminar préstamo
        prestamoRepository.deleteById(id);

        return prestamoMapper.toDto(prestamo);
    }
}
