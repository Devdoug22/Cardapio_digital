package com.devdoug.cardapio_digital.exception;

public class CardapioNotFoundException extends RuntimeException{
    public CardapioNotFoundException(Long id){
        super("Item do cardápio não encontrado com o ID: " + id);
    }
}
