package com.devdoug.cardapio_digital.controller;

import com.devdoug.cardapio_digital.dto.CardapioRequestDTO;
import com.devdoug.cardapio_digital.model.entity.Cardapio;
import com.devdoug.cardapio_digital.service.CardapioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cardapio")
@RequiredArgsConstructor
public class CardapioController {

    private final CardapioService cardapioService;

    @PostMapping
    @Operation(summary = "Salvar novo item no cardápio", description = "Cadastra um novo item no cardápio digital")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Void> salvarItem(@RequestBody @Valid CardapioRequestDTO dto) {
        cardapioService.salvarItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID", description = "Retorna um item específico do cardápio pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item encontrado", content = @Content(schema = @Schema(implementation = Cardapio.class))),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ResponseEntity<Cardapio> buscarItemById(@Parameter(description = "ID do item do cardápio") @PathVariable Long id) {
        return ResponseEntity.ok(cardapioService.buscarItemById(id));
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos os itens", description = "Retorna uma lista com todos os itens do cardápio")
    @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso", content = @Content(schema = @Schema(implementation = Cardapio.class)))
    public ResponseEntity<List<Cardapio>> listaTodos() {
        return ResponseEntity.ok(cardapioService.listaTodos());
    }

    @DeleteMapping
    @Operation(summary = "Deletar item do cardápio", description = "Remove um item do cardápio pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ResponseEntity<Void> deletarItem(@Parameter(description = "ID do item a ser deletado") @RequestParam Long id) {
        cardapioService.excluir(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item do cardápio", description = "Atualiza os dados de um item existente no cardápio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ResponseEntity<Void> attITem(
            @Parameter(description = "ID do item a ser atualizado") @PathVariable Long id,
            @RequestBody Cardapio cardapio) {
        cardapioService.attItem(cardapio, id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/desconto")
    @Operation(summary = "Aplicar desconto a um item do cardápio", description = "Aplica um desconto percentual ao preço de um item específico do cardápio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Desconto aplicado com sucesso", content = @Content(schema = @Schema(implementation = Cardapio.class))),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos (percentual negativo ou maior que 100)"),
        @ApiResponse(responseCode = "404", description = "Item do cardápio não encontrado")
    })
    public ResponseEntity<Cardapio> aplicarDesconto(
            @Parameter(description = "ID do item do cardápio") @PathVariable Long id,
            @Parameter(description = "Percentual de desconto a ser aplicado (ex: 10 para 10%)") @RequestParam BigDecimal percentual) {
        Cardapio cardapioAtualizado = cardapioService.applyDiscount(id, percentual);
        return ResponseEntity.ok(cardapioAtualizado);
    }
}
