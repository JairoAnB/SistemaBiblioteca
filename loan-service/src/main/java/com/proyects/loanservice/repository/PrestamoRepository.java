package com.proyects.loanservice.repository;

import com.proyects.loanservice.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

}
