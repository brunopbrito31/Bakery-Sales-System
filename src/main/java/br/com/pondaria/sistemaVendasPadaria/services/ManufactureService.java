package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.StockItem;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProductRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@SessionScope
public class ManufactureService {

    Insumo insumo;
    ProductService productService;
    ProductRepository productRepository;
    StockItemRepository stockItemRepository;
    InsumoRepository insumoRepository;
    StockService stockService;

    @Autowired
    public ManufactureService(Insumo insumo, ProductService productService, ProductRepository productRepository,
                              StockItemRepository stockItemRepository, InsumoRepository insumoRepository, @SessionAttribute("estoqueService") StockService stockService)
    {
        this.insumo = insumo;
        this.productService = productService;
        this.productRepository = productRepository;
        this.stockItemRepository = stockItemRepository;
        this.insumoRepository = insumoRepository;
        this.stockService = stockService;
    }

    public Product fabricarProduto(String codBarrasProdutoAlvo){
        List<Insumo> insumos = insumoRepository.metodoParaBuscarTodosInsumosDoProduto(codBarrasProdutoAlvo);
        int requisitos = 0;
        for(Insumo x: insumos){
            Optional<StockItem> itemEstoque = stockItemRepository.buscarItemEstoquePeloCodProduto(x.getCodigoBarras());
            if(itemEstoque.isPresent()){
                BigDecimal quantidadeNoEstoque = itemEstoque.get().getQuantidade();
                if(x.getQuantidade().compareTo(itemEstoque.get().getQuantidade()) >= 0){
                    requisitos = requisitos;
                }
                else{
                    stockService.retirarItemNoEstoque(x.getCodigoBarras(),x.getQuantidade(), MovementType.BAIXAINTERNA);
                    requisitos++;
                }
            }
        }
        if(requisitos == insumos.size()){
            Product productCriado = productRepository.buscarPeloCodigoBarras(codBarrasProdutoAlvo).get();
            stockService.adicionarItemNoEstoque(codBarrasProdutoAlvo,BigDecimal.valueOf(1), MovementType.FABRICACAO);
            return productCriado;
        }else{
            throw new IllegalArgumentException("Você não possui todos os itens em estoque para permanecer com a fabricação");
        }
    }

    public Insumo associarUmInsumo(String codBarrasProdutoCaseiro, String codBarrasInsumo) {
        Optional<Product> prod = productRepository.buscarPeloCodigoBarras(codBarrasProdutoCaseiro);
        if (prod.isPresent()) {
            if(prod.get().getProdutoFabricado().equals(false)) throw new IllegalArgumentException("Produto não caseiro");
            if (prod.get().getStatus().equals("INATIVO")) {
                throw new IllegalArgumentException("Não é possível trabalhar com produto que não está com o cadastro ativo");
            } else {
                Optional<StockItem> itemEstoqueProcuradoAntes = stockItemRepository.buscarItemEstoquePeloCodProduto(codBarrasProdutoCaseiro);
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
