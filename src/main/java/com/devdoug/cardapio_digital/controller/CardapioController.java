package com.devdoug.cardapio_digital.controller;

import com.devdoug.cardapio_digital.model.entity.Cardapio;
import com.devdoug.cardapio_digital.service.CardapioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cardapio")
@RequiredArgsConstructor

public class CardapioController {

    public final CardapioService cardapioService;
    
    @PostMapping
    public ResponseEntity<Void> salvarItem(@RequestBody Cardapio cardapio) {
        cardapioService.salvarItem(cardapio);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Cardapio> buscarItemById(@RequestParam  Long id) {
        return ResponseEntity.ok(cardapioService.buscarItemById(id));
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Cardapio>> listaTodos() {
        return ResponseEntity.ok(cardapioService.listaTodos());
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarItem(@RequestParam Long id) {
        cardapioService.excluir(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> attITem(@PathVariable Long id, @RequestBody Cardapio cardapio) {
        cardapioService.attItem(cardapio, id);
        return ResponseEntity.ok().build();
    }
}
