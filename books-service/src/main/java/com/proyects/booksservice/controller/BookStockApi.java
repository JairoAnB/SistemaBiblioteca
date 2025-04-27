package com.proyects.booksservice.controller;

import com.proyects.booksservice.dto.BookDTO;
import com.proyects.booksservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookStockApi {


    private final BookService bookService;

    @Autowired
    public BookStockApi(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDTO> getAllBooks(){
        return bookService.findAllBooks();
    }

    @GetMapping("/{id}")
    //PathVariable es para obtener el id de la url
    public BookDTO getBookById(@PathVariable  Long id){
        return bookService.findBookbyId(id);
    }

    @PostMapping("/create")
    //RequestBody es para obtener los datos que vienen del cuerpo de la peticion
    //PostMapping es solo para crear un recurso (Buenas practicas)
    public BookDTO createBook(@RequestBody BookDTO bookDTO){
        return bookService.createLibro(bookDTO);
    }

    @PutMapping("/update/{id}")
    //PutMapping es solo para actualizar un recurso (Buenas practicas)
    public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO){
        return bookService.updateBook(id, bookDTO);
    }

    @DeleteMapping("/delete/{id}")
    //DeleteMapping es solo para eliminar un recurso (Buenas practicas)
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

    @PutMapping("/updateStock/{id}/{stock}")
    public void updateStock(@PathVariable Long id, @PathVariable Integer stock){
        bookService.updateStock(id, stock);
    }

}
