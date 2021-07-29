package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, Long> {

    @Query("select id from tb_estoque " +
            "where produto_id = ?1")
    Long verificar(Long idProduto);

    @Query("update tb_estoque " +
            "set quantidade = ?1 " +
            "where id = ?2")
    void atualizarQuantidade(BigDecimal quantidade, Long idItemEstoque);

    @Query(value = "select tb_estoque.id from tb_estoque " +
            "inner join tb_produto " +
            "on tb_estoque.produto_id = tb_produto.id " +
            "where tb_produto.codigo_barras = ?1",nativeQuery = true)
    Long verificarPorCod(String codBarras);
}
