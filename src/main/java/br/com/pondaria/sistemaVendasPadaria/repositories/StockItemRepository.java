package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    /*
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

     */

    // Pendente de validação Só essa query está quebrando o meu código
    //Método para retornar um item de estoque pelo código de barras do produto
    @Query(value = "select tb_stock.id, " +
            "tb_stock.item_status, " +
            "tb_stock.quantity, " +
            "tb_stock.product_id from tb_stock " +
            "inner join tb_product " +
            "on tb_stock.product_id = tb_product.id " +
            "where tb_product.barcode = ?1", nativeQuery = true)
    Optional<StockItem> searchItemStockByBarcode(String barcode);

}
