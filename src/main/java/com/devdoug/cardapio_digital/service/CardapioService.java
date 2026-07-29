package com.devdoug.cardapio_digital.service;

import com.devdoug.cardapio_digital.model.entity.Cardapio;
import com.devdoug.cardapio_digital.model.repository.CardapioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@RequiredArgsConstructor // <-- O Lombok cria o construtor para os atributos 'final' automaticamente
@Service
public class CardapioService {

    // 1. Injeção via construtor (Boa prática: atributo final e sem @Autowired)
    private final CardapioRepository repository;

    public List<Cardapio> listaTodos() {
        return repository.findAll();
    }

    @Transactional // Garante a integridade da operação no banco
    public void salvarItem(Cardapio cardapio) {
        repository.saveAndFlush(cardapio);
    }

    public Cardapio buscarItemById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Item não encontrado")
        );
    }
    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void attItem (Cardapio cardapioExistente, Long id) {
        Cardapio cardapioAtt2 = buscarItemById(id);

       if(cardapioExistente.getName() != null && !cardapioExistente.getName().isBlank()) {
           cardapioAtt2.setName(cardapioExistente.getName());

       }
       repository.saveAndFlush(cardapioExistente);
    }

    @Transactional
    public Cardapio applyDiscount(Long id, BigDecimal discountPercentage) {
        if (discountPercentage.compareTo(BigDecimal.ONE) < 0 || discountPercentage.compareTo(new BigDecimal("50")) > 0) {
            throw new IllegalArgumentException("O desconto deve estar entre 1% e 50%");
        }

        Cardapio cardapio = buscarItemById(id);
        double discountMultiplier = discountPercentage.divide(new BigDecimal("100")).doubleValue();
        double discountedPrice = cardapio.getPreco() * (1 - discountMultiplier);
        cardapio.setPreco(discountedPrice);
        
        return repository.saveAndFlush(cardapio);
    }

}
