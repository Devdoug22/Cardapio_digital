package com.devdoug.cardapio_digital.service;

import com.devdoug.cardapio_digital.model.entity.Cardapio;
import com.devdoug.cardapio_digital.model.repository.CardapioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardapioServiceTest {

    @Mock
    private CardapioRepository repository;

    @InjectMocks
    private CardapioService cardapioService;

    @Test
    @DisplayName("Deve aplicar desconto com sucesso quando válido")
    void applyDiscount_Success() {
        Long id = 1L;
        BigDecimal discountPercentage = new BigDecimal("20");
        Cardapio cardapio = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(100.0)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(cardapio));
        when(repository.saveAndFlush(any(Cardapio.class))).thenReturn(cardapio);

        Cardapio result = cardapioService.applyDiscount(id, discountPercentage);

        assertNotNull(result);
        assertEquals(80.0, result.getPreco(), 0.001);
        verify(repository).findById(id);
        verify(repository).saveAndFlush(cardapio);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o desconto for maior que 50%")
    void applyDiscount_ThrowsException_WhenDiscountGreaterThan50() {
        Long id = 1L;
        BigDecimal discountPercentage = new BigDecimal("55");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cardapioService.applyDiscount(id, discountPercentage)
        );

        assertEquals("O desconto deve estar entre 1% e 50%", exception.getMessage());
        verify(repository, never()).findById(any());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o desconto for menor que 1%")
    void applyDiscount_ThrowsException_WhenDiscountLessThan1() {
        Long id = 1L;
        BigDecimal discountPercentage = new BigDecimal("0.5");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cardapioService.applyDiscount(id, discountPercentage)
        );

        assertEquals("O desconto deve estar entre 1% e 50%", exception.getMessage());
        verify(repository, never()).findById(any());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o desconto for zero")
    void applyDiscount_ThrowsException_WhenDiscountIsZero() {
        Long id = 1L;
        BigDecimal discountPercentage = BigDecimal.ZERO;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cardapioService.applyDiscount(id, discountPercentage)
        );

        assertEquals("O desconto deve estar entre 1% e 50%", exception.getMessage());
        verify(repository, never()).findById(any());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o desconto for negativo")
    void applyDiscount_ThrowsException_WhenDiscountIsNegative() {
        Long id = 1L;
        BigDecimal discountPercentage = new BigDecimal("-10");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cardapioService.applyDiscount(id, discountPercentage)
        );

        assertEquals("O desconto deve estar entre 1% e 50%", exception.getMessage());
        verify(repository, never()).findById(any());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve aplicar desconto de 1% (limite inferior)")
    void applyDiscount_Success_WhenDiscountIs1() {
        Long id = 1L;
        BigDecimal discountPercentage = BigDecimal.ONE;
        Cardapio cardapio = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(100.0)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(cardapio));
        when(repository.saveAndFlush(any(Cardapio.class))).thenReturn(cardapio);

        Cardapio result = cardapioService.applyDiscount(id, discountPercentage);

        assertNotNull(result);
        assertEquals(99.0, result.getPreco(), 0.001);
        verify(repository).findById(id);
        verify(repository).saveAndFlush(cardapio);
    }

    @Test
    @DisplayName("Deve aplicar desconto de 50% (limite superior)")
    void applyDiscount_Success_WhenDiscountIs50() {
        Long id = 1L;
        BigDecimal discountPercentage = new BigDecimal("50");
        Cardapio cardapio = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(100.0)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(cardapio));
        when(repository.saveAndFlush(any(Cardapio.class))).thenReturn(cardapio);

        Cardapio result = cardapioService.applyDiscount(id, discountPercentage);

        assertNotNull(result);
        assertEquals(50.0, result.getPreco(), 0.001);
        verify(repository).findById(id);
        verify(repository).saveAndFlush(cardapio);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando item não encontrado")
    void applyDiscount_ThrowsRuntimeException_WhenItemNotFound() {
        Long id = 1L;
        BigDecimal discountPercentage = new BigDecimal("20");

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> cardapioService.applyDiscount(id, discountPercentage)
        );

        verify(repository).findById(id);
        verify(repository, never()).saveAndFlush(any());
    }
}
