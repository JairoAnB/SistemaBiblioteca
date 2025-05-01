package com.proyects.loanservice.exception;

public class NoPrestamosRestantes extends RuntimeException {
  public NoPrestamosRestantes(String message) {
    super(message);
  }
}
