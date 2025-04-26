package com.proyects.loanservice.service;

import com.proyects.loanservice.dto.BookDTO;
import com.proyects.loanservice.dto.BookStockUpdateDto;
import com.proyects.loanservice.dto.LoanRequestDTO;
import com.proyects.loanservice.dto.PrestamoDTO;
import com.proyects.loanservice.mapper.PrestamoMapper;
import com.proyects.loanservice.model.Prestamo;
import com.proyects.loanservice.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void validateUser(Long userId) {
        //realiza un get a la api de usuarios usando rest Template
        //void class se significa practiamente decirle que no importa la respuesta del body, solo quieres saber si responde correctamente o no
        //Response entity es una clase que te permite obtener el status code de la respuesta
        ResponseEntity<Void> response = restTemplate.getForEntity(user_service_url + userId, Void.class);

        //Validacion del estado 200, 404, etc
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Usuario valido");
        } else {
            System.out.println("Usuario no valido");
        }
    }

    public void validateBook(Long bookId, Integer stock){

        try{
            //obtengo la informacion del libro

            ResponseEntity<BookDTO> bookResponse = restTemplate.getForEntity(
                    book_service_url + "{id}",
                    BookDTO.class, bookId
            );
            if(!bookResponse.getStatusCode().is2xxSuccessful() || bookResponse.getBody() == null){
                throw new RuntimeException("El libro con la id " + bookId + " no existe");
            }

            BookDTO bookDTO = bookResponse.getBody();

            //Validar el stock

            if(bookDTO.getStock() <= stock){
                throw new RuntimeException("El libro con la id " + bookId + " no tiene stock");
            }

            int nuevoStock = bookDTO.getStock() - stock;

            BookStockUpdateDto updateDto = new BookStockUpdateDto(nuevoStock);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public PrestamoDTO createPrestamo(LoanRequestDTO request, Integer stock){
        //Validar el usuario y el libro
        validateUser(request.getUsuarioPrestamoId());
        validateBook(request.getLibroPrestamoId());




        //Si existe se sigue con el proceso
        Prestamo prestamoEntity = prestamoMapper.toPrestamoEntity(request);
        prestamoEntity.setFechaPrestamo(LocalDate.now());


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
}
