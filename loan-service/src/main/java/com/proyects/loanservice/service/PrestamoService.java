package com.proyects.loanservice.service;

import com.proyects.loanservice.dto.BookDTO;
import com.proyects.loanservice.dto.BookStockUpdateDto;
import com.proyects.loanservice.dto.LoanRequestDTO;
import com.proyects.loanservice.dto.PrestamoDTO;
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
    public void validateUser(Long usuarioPrestamoId) {
        //realiza un get a la api de usuarios usando rest Template
        //void class se significa practiamente decirle que no importa la respuesta del body, solo quieres saber si responde correctamente o no
        //Response entity es una clase que te permite obtener el status code de la respuesta
        ResponseEntity<Void> response = restTemplate.getForEntity(user_service_url + usuarioPrestamoId, Void.class);

        //Validacion del estado 200, 404, etc
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Usuario valido");
        } else {
            System.out.println("Usuario no valido");
        }
    }

    public void validateBook(Long libroId){

        try{
            //obtengo la informacion del libro
            ResponseEntity<BookDTO> bookResponse = restTemplate.getForEntity(
                    book_service_url + libroId,
                    BookDTO.class
            );
            if(!bookResponse.getStatusCode().is2xxSuccessful() || bookResponse.getBody() == null){
                throw new RuntimeException("El libro con la id " + libroId + " no existe");
            }

            BookDTO bookDTO = bookResponse.getBody();

            //Validar el stock
            if(bookDTO.getStock() <= 0){
                throw new RuntimeException("El libro con la id " + libroId + " no tiene stock");
            }

            BookStockUpdateDto updateDto = new BookStockUpdateDto(bookDTO.getStock() - 1);

            //Actualizar el stock del libro
            ResponseEntity<BookStockUpdateDto> updateDtoResponse = restTemplate.exchange //Utilizo restTemplate exchange es para poder realizar solicitudes de varios tipos.
                                                                                            // en este caso put ya que actualizaremos recursos existentes
                    (book_service_url_stock + libroId + "/" + (bookDTO.getStock() - 1), //Se construye la URL del microservicio, contenando el libro ID, slash y el stock actual, cual se le desminiye uno.
                            HttpMethod.PUT, new HttpEntity<>(updateDto) //HttpEntity se ocupa para incluir el cuerpo de la solicitud en formato json, updateDto es un objeto que contiene el stock decrementado
                            , BookStockUpdateDto.class); // El tipo de respuesta esperado se dificne como bookStockUpdateDto.class esto se significa que el cuerpo de la respuesta espera ser un objeto de la clase.
            //La respuesta se almacena en updateDtoResponse, que es el objeto creado por responseEntity, este objeto contiene tanto el cuerpo de la respuesta, como el codigo
            System.out.println("Stock actualizado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
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
