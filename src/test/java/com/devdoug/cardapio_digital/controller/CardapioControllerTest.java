package com.devdoug.cardapio_digital.controller;

import com.devdoug.cardapio_digital.model.entity.Cardapio;
import com.devdoug.cardapio_digital.service.CardapioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardapioController.class)
class CardapioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardapioService cardapioService;

    @Test
    @DisplayName("Deve aplicar desconto com sucesso e retornar HTTP 200")
    void aplicarDesconto_Success() throws Exception {
        Long id = 1L;
        BigDecimal percentual = new BigDecimal("20.00");

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(80.0)
                .build();

        when(cardapioService.applyDiscount(eq(id), any(BigDecimal.class))).thenReturn(cardapioAtualizado);

        mockMvc.perform(patch("/cardapio/{id}/desconto", id)
                        .param("percentual", percentual.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.preco").value(80.0));
    }

    @Test
    @DisplayName("Deve retornar HTTP 400 quando o desconto for inválido (> 50% ou < 1%)")
    void aplicarDesconto_ThrowsBadRequest_WhenDiscountInvalid() throws Exception {
        Long id = 1L;
        BigDecimal percentualInvalido = new BigDecimal("55.00");

        when(cardapioService.applyDiscount(eq(id), any(BigDecimal.class)))
                .thenThrow(new IllegalArgumentException("O desconto deve estar entre 1% e 50%"));

        mockMvc.perform(patch("/cardapio/{id}/desconto", id)
                        .param("percentual", percentualInvalido.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar HTTP 404 quando o item do cardápio não for encontrado")
    void aplicarDesconto_ThrowsNotFound_WhenItemDoesNotExist() throws Exception {
        Long idInexistente = 999L;
        BigDecimal percentual = new BigDecimal("10.00");

        when(cardapioService.applyDiscount(eq(idInexistente), any(BigDecimal.class)))
                .thenThrow(new NoSuchElementException("Item do cardápio não encontrado com ID: " + idInexistente));

        mockMvc.perform(patch("/cardapio/{id}/desconto", idInexistente)
                        .param("percentual", percentual.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve aplicar desconto de 50% (limite superior)")
    void aplicarDesconto_Success_WhenDiscountIs50() throws Exception {
        Long id = 1L;
        BigDecimal percentual = new BigDecimal("50.00");

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(50.0)
                .build();

        when(cardapioService.applyDiscount(eq(id), any(BigDecimal.class))).thenReturn(cardapioAtualizado);

        mockMvc.perform(patch("/cardapio/{id}/desconto", id)
                        .param("percentual", percentual.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.preco").value(50.0));
    }

    @Test
    @DisplayName("Deve aplicar desconto de 1% (limite inferior)")
    void aplicarDesconto_Success_WhenDiscountIs1() throws Exception {
        Long id = 1L;
        BigDecimal percentual = new BigDecimal("1.00");

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(99.0)
                .build();

        when(cardapioService.applyDiscount(eq(id), any(BigDecimal.class))).thenReturn(cardapioAtualizado);

        mockMvc.perform(patch("/cardapio/{id}/desconto", id)
                        .param("percentual", percentual.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.preco").value(99.0));
    }
}