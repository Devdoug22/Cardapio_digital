package com.devdoug.cardapio_digital.exception;

public class FoodNotFoundException extends RuntimeException {
    public FoodNotFoundException(Long id) {
        super("Produto do cardápio não encontrado com o ID: " + id);
    }
}