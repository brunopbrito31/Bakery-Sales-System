package br.com.pondaria.sistemaVendasPadaria.repositories;

import br.com.pondaria.sistemaVendasPadaria.model.entities.fabricacao.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {


    @Query(value = "select tb_insumo.id, " +
            "tb_insumo.codigo_barras, " +
            "tb_insumo.quantidade, " +
            "tb_insumo.codigobarrasproudtopai " +
            "from tb_insumo " +
            "where tb_insumo.codigoBarrasProdutopai = ?1", nativeQuery = true)
    List<Insumo> metodoParaBuscarTodosInsumosDoProduto(String codBarras);
}
