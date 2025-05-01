package com.proyects.booksservice.service;

import com.proyects.booksservice.dto.BookDTO;
import com.proyects.booksservice.excepcion.LibroNoActualizado;
import com.proyects.booksservice.excepcion.LibroNoEliminado;
import com.proyects.booksservice.excepcion.LibroNoEncontrado;
import com.proyects.booksservice.mapper.BookMapper;
import com.proyects.booksservice.model.Book;
import com.proyects.booksservice.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class BookService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }


    @Transactional(readOnly = true) //Opcion de buena practica para metodos de solo lectura
    //OCUPAR SIEMPRE LA ANOTACION CON LA IMPORTACION DE SPRINGBOOT
    public BookDTO findBookbyId(Long id){
        Book bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontrado("El libro con la id " + id + " no existe"));

        //Se convierte la entidad a el dto
        return  bookMapper.toDto(bookEntity);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> findAllBooks(){
        List<Book> bookEntidades = bookRepository.findAll();

        //Se usa el mapper para poder convertir la lista entera de entidades a dtos

        return  bookMapper.toDtoList(bookEntidades);
    }

    @Transactional //Aqui se deja sin ninguna anotacion pro que es una operacion de escritura y modificacion
    public BookDTO createLibro(BookDTO bookDTO){
        Book bookEntity = bookMapper.toEntity(bookDTO);

        Book EntidadGuardada = bookRepository.save(bookEntity);

        return bookMapper.toDto(EntidadGuardada);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO){
        //primero tenemos que buscar si existe el libro
        Book bookExistente = bookRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontrado("El libro con la id " + id + " no existe"));
        //Segundo convertimos el DTO a una entidad temporal

        try{
            Book temporalInfo = bookMapper.toEntity(bookDTO);

            //Tercero actualizamos la info con los campos del temporalInfo
            bookExistente.setTitulo(temporalInfo.getTitulo());
            bookExistente.setAutorName(temporalInfo.getAutorName());
            bookExistente.setIsbn(temporalInfo.getIsbn());
            bookExistente.setStock(temporalInfo.getStock());

            //Cuarto guardamos la entidad actualizada
            Book bookEntity = bookRepository.save(bookExistente);

            //Quinto convertimos la entidad actualizada a un DTO
            return bookMapper.toDto(bookEntity);

        } catch (Exception e) {
            throw new LibroNoActualizado("El libro con la id " + id + " no se pudo actualizar");
        }
    }

    @Transactional
    public void deleteBook(Long id){
        if(!bookRepository.existsById(id)){
            throw new LibroNoEncontrado("El libro con la id " + id + " no existe");
        }

        try {
            bookRepository.deleteById(id);

        } catch (Exception e) {
            throw new LibroNoEliminado("El libro con la id " + id + " no se pudo eliminar");
        }
    }

    @Transactional
    public BookDTO updateStock(Long id, Integer stock){
        Book bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontrado("El libro con la id " + id + " no existe"));
        try{
            bookEntity.setStock(stock);
            Book actualizado = bookRepository.save(bookEntity);
            return bookMapper.toDto(actualizado);
        }catch (Exception e){
            throw new LibroNoActualizado("El libro con la id " + id + " no se pudo actualizar");
        }
    }
}
