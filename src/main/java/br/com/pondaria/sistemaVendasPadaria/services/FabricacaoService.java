package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.fabricacao.Insumo;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.InsumoRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
public class FabricacaoService {

    Insumo insumo;
    ProdutoService produtoService;
    ProdutoRepository produtoRepository;
    ItemEstoqueRepository itemEstoqueRepository;
    InsumoRepository insumoRepository;
    EstoqueService estoqueService;

    @Autowired
    public FabricacaoService(Insumo insumo, ProdutoService produtoService, ProdutoRepository produtoRepository,
                             ItemEstoqueRepository itemEstoqueRepository, InsumoRepository insumoRepository, @SessionAttribute("estoqueService") EstoqueService estoqueService)
    {
        this.insumo = insumo;
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.insumoRepository = insumoRepository;
        this.estoqueService = estoqueService;
    }

    public Produto fabricarProduto(String codBarrasProdutoAlvo){
        List<Insumo> insumos = insumoRepository.metodoParaBuscarTodosInsumosDoProduto(codBarrasProdutoAlvo);
        int requisitos = 0;
        for(Insumo x: insumos){
            Optional<ItemEstoque> itemEstoque = itemEstoqueRepository.buscarItemEstoquePeloCodProduto(x.getCodigoBarras());
            if(itemEstoque.isPresent()){
                BigDecimal quantidadeNoEstoque = itemEstoque.get().getQuantidade();
                if(x.getQuantidade().compareTo(itemEstoque.get().getQuantidade()) >= 0){
                    requisitos = requisitos;
                }
                else{
                    estoqueService.retirarItemNoEstoque(x.getCodigoBarras(),x.getQuantidade(), TipoMovimentacao.BAIXAINTERNA);
                    requisitos++;
                }
            }
        }
        if(requisitos == insumos.size()){
            Produto produtoCriado = produtoRepository.buscarPeloCodigoBarras(codBarrasProdutoAlvo).get();
            estoqueService.adicionarItemNoEstoque(codBarrasProdutoAlvo,BigDecimal.valueOf(1),TipoMovimentacao.FABRICACAO);
            return produtoCriado;
        }else{
            throw new IllegalArgumentException("Você não possui todos os itens em estoque para permanecer com a fabricação");
        }
    }

    public Insumo associarUmInsumo(String codBarrasProdutoCaseiro, String codBarrasInsumo) {
        Optional<Produto> prod = produtoRepository.buscarPeloCodigoBarras(codBarrasProdutoCaseiro);
        if (prod.isPresent()) {
            if(prod.get().getProdutoFabricado().equals(false)) throw new IllegalArgumentException("Produto não caseiro");
            if (prod.get().getStatus().equals("INATIVO")) {
                throw new IllegalArgumentException("Não é possível trabalhar com produto que não está com o cadastro ativo");
            } else {
                Optional<ItemEstoque> itemEstoqueProcuradoAntes = itemEstoqueRepository.buscarItemEstoquePeloCodProduto(codBarrasProdutoCaseiro);
                if (!itemEstoqueProcuradoAntes.isPresent())
                    throw new IllegalArgumentException("Não há o produto no estoque");
                else {
                    ItemEstoque itemEstoqueProcurado = itemEstoqueProcuradoAntes.get();
                    return Insumo.builder().codigoBarrasProdutoPai(codBarrasProdutoCaseiro).codigoBarras(codBarrasInsumo).build();
                }
            }
        }else{
            throw new IllegalArgumentException("Produto não cadastrado");
        }
    }
}
