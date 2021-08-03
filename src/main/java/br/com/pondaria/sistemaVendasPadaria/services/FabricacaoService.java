package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.StockItem;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.model.entities.fabricacao.Insumo;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
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

    public Product fabricarProduto(String codBarrasProdutoAlvo){
        List<Insumo> insumos = insumoRepository.metodoParaBuscarTodosInsumosDoProduto(codBarrasProdutoAlvo);
        int requisitos = 0;
        for(Insumo x: insumos){
            Optional<StockItem> itemEstoque = itemEstoqueRepository.buscarItemEstoquePeloCodProduto(x.getCodigoBarras());
            if(itemEstoque.isPresent()){
                BigDecimal quantidadeNoEstoque = itemEstoque.get().getQuantidade();
                if(x.getQuantidade().compareTo(itemEstoque.get().getQuantidade()) >= 0){
                    requisitos = requisitos;
                }
                else{
                    estoqueService.retirarItemNoEstoque(x.getCodigoBarras(),x.getQuantidade(), MovementType.BAIXAINTERNA);
                    requisitos++;
                }
            }
        }
        if(requisitos == insumos.size()){
            Product productCriado = produtoRepository.buscarPeloCodigoBarras(codBarrasProdutoAlvo).get();
            estoqueService.adicionarItemNoEstoque(codBarrasProdutoAlvo,BigDecimal.valueOf(1), MovementType.FABRICACAO);
            return productCriado;
        }else{
            throw new IllegalArgumentException("Você não possui todos os itens em estoque para permanecer com a fabricação");
        }
    }

    public Insumo associarUmInsumo(String codBarrasProdutoCaseiro, String codBarrasInsumo) {
        Optional<Product> prod = produtoRepository.buscarPeloCodigoBarras(codBarrasProdutoCaseiro);
        if (prod.isPresent()) {
            if(prod.get().getProdutoFabricado().equals(false)) throw new IllegalArgumentException("Produto não caseiro");
            if (prod.get().getStatus().equals("INATIVO")) {
                throw new IllegalArgumentException("Não é possível trabalhar com produto que não está com o cadastro ativo");
            } else {
                Optional<StockItem> itemEstoqueProcuradoAntes = itemEstoqueRepository.buscarItemEstoquePeloCodProduto(codBarrasProdutoCaseiro);
                if (!itemEstoqueProcuradoAntes.isPresent())
                    throw new IllegalArgumentException("Não há o produto no estoque");
                else {
                    StockItem stockItemProcurado = itemEstoqueProcuradoAntes.get();
                    return Insumo.builder().codigoBarrasProdutoPai(codBarrasProdutoCaseiro).codigoBarras(codBarrasInsumo).build();
                }
            }
        }else{
            throw new IllegalArgumentException("Produto não cadastrado");
        }
    }
}
