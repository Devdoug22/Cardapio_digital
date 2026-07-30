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

/**
 * Classe de teste unitário/integração leve para o {@link CardapioController}.
 * <p>
 * A anotação {@code @WebMvcTest} isola a camada Web (Controller), subindo apenas
 * os componentes MVC necessários, garantindo testes extremamente rápidos e focados.
 */
@WebMvcTest(CardapioController.class)
class CardapioControllerTest {

    /**
     * Ferramenta do Spring para simular requisições HTTP (GET, POST, PATCH, etc.)
     * sem a necessidade de subir um servidor Tomcat/Jetty real.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Cria um "dublê" (Mock) da camada de serviço.
     * Impede que o teste dependa do banco de dados ou da implementação real da Service.
     */
    @MockitoBean
    private CardapioService cardapioService;

    /**
     * Testa o fluxo principal (Caminho Feliz) de aplicação de desconto no item.
     * <p>
     * <b>Dado que:</b> O item existe e o percentual informado é válido (20%).<br>
     * <b>Quando:</b> A requisição PATCH for enviada.<br>
     * <b>Então:</b> Deve retornar status HTTP 200 OK com o JSON do item atualizado.
     */
    @Test
    @DisplayName("Deve aplicar desconto com sucesso e retornar HTTP 200")
    void aplicarDesconto_Success() throws Exception {
        // 1. ARRANGEMENT (Preparação)
        Long id = 1L;
        BigDecimal percentual = new BigDecimal("20.00");

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(80.0)
                .build();

        // Ensina o Mockito o que o serviço deve retornar quando for chamado
        when(cardapioService.applyDiscount(eq(id), any(BigDecimal.class)))
                .thenReturn(cardapioAtualizado);

        // 2. ACTION & ASSERTION (Execução e Validação)
        mockMvc.perform(patch("/cardapio/{id}/desconto", id)
                        .param("percentual", percentual.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.preco").value(80.0));
    }

    /**
     * Testa a regra de negócio para percentual de desconto inválido (Exceeding Bounds).
     * <p>
     * <b>Dado que:</b> O percentual informado é superior a 50% (55%).<br>
     * <b>Quando:</b> A Service lança uma {@link IllegalArgumentException}.<br>
     * <b>Então:</b> O Handler da Controller deve mapear a exceção para HTTP 400 Bad Request.
     */
    @Test
    @DisplayName("Deve retornar HTTP 400 quando o desconto for inválido (> 50% ou < 1%)")
    void aplicarDesconto_ThrowsBadRequest_WhenDiscountInvalid() throws Exception {
        // ARRANGEMENT
        Long id = 1L;
        BigDecimal percentualInvalido = new BigDecimal("55.00");

        // Simula que a Service recusou o parâmetro e lançou exceção de argumento inválido
        when(cardapioService.applyDiscount(eq(id), any(BigDecimal.class)))
                .thenThrow(new IllegalArgumentException("O desconto deve estar entre 1% e 50%"));

        // ACTION & ASSERTION
        mockMvc.perform(patch("/cardapio/{id}/desconto", id)
                        .param("percentual", percentualInvalido.toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * Testa o comportamento da API ao tentar alterar um item que não existe no banco.
     * <p>
     * <b>Dado que:</b> O ID fornecido não existe no sistema.<br>
     * <b>Quando:</b> A Service lança uma {@link NoSuchElementException}.<br>
     * <b>Então:</b> A Controller deve retornar HTTP 404 Not Found.
     */
    @Test
    @DisplayName("Deve retornar HTTP 404 quando o item do cardápio não for encontrado")
    void aplicarDesconto_ThrowsNotFound_WhenItemDoesNotExist() throws Exception {
        // ARRANGEMENT
        Long idInexistente = 999L;
        BigDecimal percentual = new BigDecimal("10.00");

        // Simula que a Service não encontrou o registro no banco de dados
        when(cardapioService.applyDiscount(eq(idInexistente), any(BigDecimal.class)))
                .thenThrow(new NoSuchElementException("Item do cardápio não encontrado com ID: " + idInexistente));

        // ACTION & ASSERTION
        mockMvc.perform(patch("/cardapio/{id}/desconto", idInexistente)
                        .param("percentual", percentual.toString()))
                .andExpect(status().isNotFound());
    }

    /**
     * Teste de Valor Limite (Boundary Testing): Teto máximo permitido (50%).
     * <p>
     * Garante que o valor limite de 50% é processado como uma requisição bem-sucedida.
     */
    @Test
    @DisplayName("Deve aplicar desconto de 50% (limite superior)")
    void aplicarDesconto_Success_WhenDiscountIs50() throws Exception {
        // ARRANGEMENT
        Long id = 1L;
        BigDecimal percentual = new BigDecimal("50.00");

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(50.0)
                .build();

        when(cardapioService.applyDiscount(eq(id), any(BigDecimal.class)))
                .thenReturn(cardapioAtualizado);

        // ACTION & ASSERTION
        mockMvc.perform(patch("/cardapio/{id}/desconto", id)
                        .param("percentual", percentual.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.preco").value(50.0));
    }

    /**
     * Teste de Valor Limite (Boundary Testing): Piso mínimo permitido (1%).
     * <p>
     * Garante que o valor limite de 1% é processado como uma requisição bem-sucedida.
     */
    @Test
    @DisplayName("Deve aplicar desconto de 1% (limite inferior)")
    void aplicarDesconto_Success_WhenDiscountIs1() throws Exception {
        // ARRANGEMENT
        Long id = 1L;
        BigDecimal percentual = new BigDecimal("1.00");

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(id)
                .name("Hambúrguer")
                .preco(99.0)
                .build();

        when(cardapioService.applyDiscount(eq(id), any(BigDecimal.class)))
                .thenReturn(cardapioAtualizado);

        // ACTION & ASSERTION
        mockMvc.perform(patch("/cardapio/{id}/desconto", id)
                        .param("percentual", percentual.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.preco").value(99.0));
    }
}