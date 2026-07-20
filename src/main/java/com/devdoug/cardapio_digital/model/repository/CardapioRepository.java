package com.devdoug.cardapio_digital.model.repository;

import com.devdoug.cardapio_digital.model.entity.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardapioRepository extends JpaRepository<Cardapio,Long> {

}
