package com.proyects.booksservice.controller;

import com.proyects.booksservice.dto.BookDTO;
import com.proyects.booksservice.excepcion.LibroNoCreado;
import com.proyects.booksservice.excepcion.LibroNoEncontrado;
import com.proyects.booksservice.model.Book;
import com.proyects.booksservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BookDTO>>getAllBooks(){

       List<BookDTO> libros = bookService.findAllBooks();
        if(libros.isEmpty()){
            return ResponseEntity.noContent().build(); //SI NO HAY DATOS SE RETORNA NO CONTENT 204
        }
        return ResponseEntity.ok(libros); //SI HAY DATOS SE RETORNA OK 200
    }

    @GetMapping("/{id}")
    //PathVariable es para obtener el id de la url
    public ResponseEntity<BookDTO> getBookById(@PathVariable  Long id){

        BookDTO dto = bookService.findBookbyId(id);

        //ResponseEntity es para devolver una respuesta con un status y un body
        //Dependiendo el contexto se envia el responseEntity con el status y el body si es un 200
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    //RequestBody es para obtener los datos que vienen del cuerpo de la peticion
    //PostMapping es solo para crear un recurso (Buenas practicas)
    public ResponseEntity<String> createBook(@RequestBody BookDTO bookDTO){

        try{

            BookDTO libroCreado = bookService.createLibro(bookDTO);

            //ResponseEntity es para devolver una respuesta con un status pero otro tipo de status como 201 etc y un body
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("El libro fue creado correctamente.");
        } catch (Exception e) {

            throw new LibroNoCreado("El libro no se pudo crear");

        }

    }

    @PutMapping("/update/{id}")
    //PutMapping es solo para actualizar un recurso (Buenas practicas)
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO){

        BookDTO dto = bookService.updateBook(id, bookDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("El libro con la id " + id + " fue actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    //DeleteMapping es solo para eliminar un recurso (Buenas practicas)
    public ResponseEntity<String> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.ok("El libro con la id " + id + " fue eliminado correctamente");
    }

    @PutMapping("/updateStock/{id}/{stock}")
    public ResponseEntity<String> updateStock(@PathVariable Long id, @PathVariable Integer stock){
       BookDTO actualizado = bookService.updateStock(id, stock);

       return ResponseEntity
               .status(HttpStatus.OK)
               .body("El stock del libro con la id " + id + " fue actualizado a " + stock + " correctamente");
    }

}
