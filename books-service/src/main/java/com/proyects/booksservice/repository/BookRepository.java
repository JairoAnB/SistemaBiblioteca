package com.proyects.booksservice.repository;


import com.proyects.booksservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
