package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.users.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository <Employee, Long> {
}
