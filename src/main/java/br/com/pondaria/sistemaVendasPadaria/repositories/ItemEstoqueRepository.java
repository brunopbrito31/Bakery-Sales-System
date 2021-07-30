package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Repository
public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, Long> {

    @Query("select id from tb_estoque " +
            "where produto_id = ?1")
    Long verificar(Long idProduto);

    @Transactional
    @Modifying
    @Query("update tb_estoque " +
            "set quantidade = ?1 " +
            "where id = ?2")
    void atualizarQuantidade(BigDecimal quantidade, Long idItemEstoque);

    @Query(value = "select tb_estoque.id from tb_estoque " +
            "inner join tb_produto " +
            "on tb_estoque.produto_id = tb_produto.id " +
            "where tb_produto.codigo_barras = ?1", nativeQuery = true)
    Long verificarPorCod(String codBarras);


    // Pendente de validação Só essa query está quebrando o meu código
    //Método para retornar um item de estoque pelo código de barras do produto
    /*@Query(value = "select tb_estoque.id, " +
            "tb_estoque.ativo, " + "tb_estoque.quantidade, " +

            "tb_produto.codigo_barras, " + "tb_produto.nome " +
            "tb_produto.peso_unitario " + "tb_produto.produto_fabricado " +
            "tb_produto.status " + "tb_produto.unidade_medida " +
            "tb_produto.valor_custo " + "tb_produto.valor_de_venda" +
            " from tb_estoque " + "inner join tb_produto " + "on tb_estoque.produto_id = tb_produto.id " +
            "where tb_produto.codigo_barras = ?1")
    Optional<ItemEstoque> buscarItemEstoquePeloCodProduto(String codBarras);*/

}
