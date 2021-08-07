package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

/*

    @Query("select count(descricao) from tb_produto " +
            "where codigo_barras = ?1")
    Integer verificar(String codigoBarras);

    @Query("select id from tb_produto " +
            "where codigo_barras = ?1")
    Long buscarProdutoPeloCodigo(String codigo);

*/



    //Atualizados no dia 07/08/2021 :

    @Transactional
    @Modifying
    @Query(value = "update tb_product " +
            "set description = ?1 " +
            "where barcode = ?2",nativeQuery = true)
    void udpdateDescriptionOfProduct(String description, String barcode);

    @Query(value = "select id, barcode, description, unitWeight, unitMeasure, costValue, saleValue, status from tb_product where description like ?1", nativeQuery = true)
    List<Product> searchByDescription(String description);

    @Query(value = "select id, barcode, description, unitWeight, unitMeasure, costValue, saleValue, status from tb_product where barcode = ?1", nativeQuery = true)
    Optional<Product> searchProductByBarcode(String barcode);

    @Query(value = "select id, barcode, description, unitWeight, unitMeasure, costValue, saleValue, status from tb_product where status= ?1", nativeQuery = true)
    List<Product> searchProductsByStatus(String status);

    @Transactional
    @Modifying
    @Query(value = "update tb_product " +
            "set description = ?1, costValue = ?2, unitWeight = ?3, " +
            "unitMeasure = ?4, saleValue = ?5 " +
            "where barcode = ?6",nativeQuery = true)
    void updateProduct(String description, BigDecimal costValue, BigDecimal unitWight, String unitMeasure, BigDecimal saleValue, String barcode);

    @Transactional
    @Modifying
    @Query(value = "update tb_product " +
            "set status = ?1 " +
            "where barcode = ?2",nativeQuery = true)
    void updateProductStatus(String status, String barcode);
}
