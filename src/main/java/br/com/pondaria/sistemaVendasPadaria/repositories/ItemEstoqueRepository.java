package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, Long> {

    @Query("select id from tb_item_estoque " +
            "where produto_id = ?1")
    Long verificar(Long idProduto);

    @Query("update tb_item_estoque " +
            "set quantidade = ?1 " +
            "where id = ?2")
    void atualizarQuantidade(BigDecimal quantidade, Long idItemEstoque);

    @Query(value = "select id from tb_item_estoque t " +
            "inner join tb_produto c " +
            "on t.produto_id = c.id " +
            "where c.codigo_barras = ?1",nativeQuery = true)
    Long verificarPorCod(String codBarras);
}
