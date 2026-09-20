package com.devdoug.cardapio_digital.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CardapioRequestDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @NotBlank(message = "O título do produto é obrigatório")
        String name,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @NotBlank(message = "A URL da imagem é obrigatória")
        String imagemUrl,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @NotBlank(message = "A descrição do produto é obrigatória")
        String descricao,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        Integer price
) {}
