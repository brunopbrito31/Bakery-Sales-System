/*package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.fabricacao.Fabricacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FabricacaoService {

    private ProdutoRepository produtoRepository;
    private EstoqueService estoqueService;
    private Fabricacao fabricacao;

    @Autowired
    public FabricacaoService(ProdutoRepository produtoRepository, EstoqueService estoqueService, Fabricacao fabricacao) {
        this.produtoRepository = produtoRepository;
        this.estoqueService = estoqueService;
        this.fabricacao = fabricacao;
    }

    public void cadastrarInsumos(Produto produto, Map<String, BigDecimal> insumos){
        fabricacao.setInsumos(insumos);
    }

    public Map<String,BigDecimal> criarListaInsumos(List<Produto> materias,List<BigDecimal> quantidades ){
        Fabricacao.builder().produto(new Produto()).insumos()
    }




    public void fabricarProduto(BigDecimal quantidade){

        Produto produtoFabricado = this.fabricacao.getProduto();
        Map<String,BigDecimal> insumos = this.fabricacao.getInsumos();

        Set<String> codInsumos = insumos.keySet();

        //


        //EntrySet
        public void listarContas() {
            for (Map.Entry<String, BigDecimal> itemInsumo : insumos.entrySet()) {
                String codBarras = itemInsumo.getKey();

            }
        }



    }



}
*/