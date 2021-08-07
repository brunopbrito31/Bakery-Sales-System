package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.StockItem;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.ProductStatus;
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


    void atualizarQuantidade(BigDecimal quantidade, Long idItemEstoque);

    @Query(value = "select tb_estoque.id from tb_estoque " +
            "inner join tb_produto " +
            "on tb_estoque.produto_id = tb_produto.id " +
            "where tb_produto.codigo_barras = ?1", nativeQuery = true)
    Long verificarPorCod(String codBarras);

     */


    // Atualizado 07/08
    @Query(value = "select tb_stock.id, " +
            "tb_stock.item_status, " +
            "tb_stock.quantity, " +
            "tb_stock.product_id from tb_stock " +
            "inner join tb_product " +
            "on tb_stock.product_id = tb_product.id " +
            "where tb_product.barcode = ?1", nativeQuery = true)
    Optional<StockItem> searchItemStockByBarcode(String barcode);

    // Atualizado 07/08
    @Transactional
    @Modifying
    @Query(value = "update tb_stock " +
            "set quantity = ?1, " +
            "item_status = ?2 " +
            "where id = ?3",nativeQuery = true)
    void updateQuantityAndStatus(BigDecimal quantity, String productStatus, Long stockId);
}
