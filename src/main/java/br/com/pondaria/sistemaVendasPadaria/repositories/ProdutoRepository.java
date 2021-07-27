package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Fazer só a ligação do atributo, verificar sintaxe sql ?
    //@Query("SELECT * FROM tb_produto WHERE tb_produto.codigoBarras = ?")
    //public Optional<Produto> verificar(String codigoBarras);*/

}
