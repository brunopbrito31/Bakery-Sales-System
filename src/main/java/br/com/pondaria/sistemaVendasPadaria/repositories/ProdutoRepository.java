package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {


    @Query("select count(descricao) from tb_produto " +
            "where codigo_barras = ?1")
    Integer verificar(String codigoBarras);

    @Query("select id from tb_produto " +
            "where codigo_barras = ?1")
    Long buscarProdutoPeloCodigo(String codigo);

    // Método que atualiza de descrição de um produto que foi buscado pelo código de barras
    @Transactional
    @Modifying
    @Query(value = "update tb_produto " +
            "set nome = ?1 " +
            "where codigo_barras = ?2")
    void atualizarDescricaoProduto(String descricao, String codBarras);


    // Método que atualiza um produto inteiro que foi buscado pelo código de barras
    @Transactional
    @Modifying
    @Query(value = "update tb_produto " +
            "set nome = ?1, valor_custo = ?2, peso_unitario = ?3, produto_fabricado = ?4, " +
            "unidade_medida = ?5, valor_de_venda = ?6 " +
            "where codigo_barras = ?7")
    void atualizarProdutoInteiro(String nome, BigDecimal valorCusto, BigDecimal pesoUnitario, Boolean produtoFabricado,
                                 String unidadeMedida, BigDecimal valorVenda, String codBarras);


    // Método que desativa ou ativa um produto no cadastro de produtos
    @Transactional
    @Modifying
    @Query(value = "update tb_produto " +
            "set status = INATIVO " +
            "where codigo_barras = ?1")
    void inativarAtivarCadastroProduto(String codBarras, String statusNovo);


    @Query(value = "select id, codigo_barras, nome, peso_unitario, produto_fabricado, unidade_medida, valor_custo, valor_de_venda, status from tb_produto where nome = ?1", nativeQuery = true)
    Optional<Produto> buscarPelaDescricao(String descricao);

    @Query(value = "select id, codigo_barras, nome, peso_unitario, produto_fabricado, unidade_medida, valor_custo, valor_de_venda, status from tb_produto where codigo_barras = ?1", nativeQuery = true)
    Optional<Produto> buscarPeloCodigoBarras(String codBarras);

    @Query(value = "select id, codigo_barras, nome, peso_unitario, produto_fabricado, unidade_medida, valor_custo, valor_de_venda, status from tb_produto where status= ?1", nativeQuery = true)
    List<Produto> buscarProdutosAtivos(String status);

}
