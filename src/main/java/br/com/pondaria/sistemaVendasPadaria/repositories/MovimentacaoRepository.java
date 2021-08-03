package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movement, Long> {
}
