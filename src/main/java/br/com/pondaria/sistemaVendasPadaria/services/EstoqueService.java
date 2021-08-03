package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.StockItem;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.Movement;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.MovimentacaoRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@SessionScope
public class EstoqueService {

    private ItemEstoqueRepository itemEstoqueRepository;

    private MovimentacaoRepository movimentacaoRepository;

    private ProdutoRepository produtoRepository;

    private List<StockItem> produtosArmazenados;

    private List<Movement> movimentacoesDoEstoque;


    @Autowired
    public EstoqueService(ItemEstoqueRepository itemEstoqueRepository, MovimentacaoRepository movimentacaoRepository, ProdutoRepository produtoRepository, List<StockItem> produtosArmazenados, List<Movement> movimentacoesDoEstoque) {
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.produtosArmazenados = produtosArmazenados;
        this.movimentacoesDoEstoque = movimentacoesDoEstoque;
        sincronizarComBanco();
    }

    // ok - Testar
    public Optional<StockItem> verificarEstoqueProduto(String codBarras) {
        Optional<StockItem> itemEstoqueSelecionado = itemEstoqueRepository.buscarItemEstoquePeloCodProduto(codBarras);
        if (!itemEstoqueSelecionado.isPresent()) return null; // pensar na lógica de criar um estoque
        return itemEstoqueSelecionado;
    }

    public void adicionarItemNoEstoque(String codProduto, BigDecimal quantidade, MovementType movementType) {
        if (quantidade.compareTo(BigDecimal.valueOf(0)) <= 0) throw new IllegalArgumentException("Quantidade inválida");
        Product productTeste = produtoRepository.buscarPeloCodigoBarras(codProduto).get(); // Pegar o produto que será movimentado
        Movement movimentacao;
        if (productTeste.getStatus().equals("INATIVO"))
            throw new IllegalArgumentException("Não pode manipular um produto que está com o cadastro desativado");
        Optional<StockItem> itemTeste = itemEstoqueRepository.buscarItemEstoquePeloCodProduto(codProduto);
        if (itemTeste.isPresent()) { // Caso já haja um item de estoque com o produto escolhido
            BigDecimal qtdAntiga = itemTeste.get().getQuantidade();
            itemTeste.get().setQuantidade(qtdAntiga.add(quantidade));
            itemTeste.get().setAtivo(true);
            itemEstoqueRepository.atualizarQuantidade(itemTeste.get().getQuantidade(), itemTeste.get().getId());
            movimentacao = Movement.builder()
                    .dataMovimentacao(Date.from(Instant.now()))
                    .productMovimentado(productTeste)
                    .quantidade(quantidade)
                    .tipo(movementType)
                    .build();
        } else { // ------------------  Caso não haja um item de estoque com o produto escolhido
            StockItem stockItemNovo = StockItem.builder()
                    .quantidade(quantidade)
                    .product(productTeste)
                    .ativo(true).build();
            itemEstoqueRepository.save(stockItemNovo);
            movimentacao = Movement.builder()
                    .dataMovimentacao(Date.from(Instant.now()))
                    .productMovimentado(productTeste)
                    .quantidade(quantidade)
                    .tipo(movementType)
                    .build();
        }
        movimentacaoRepository.save(movimentacao);
        sincronizarComBanco();
    }

    public void retirarItemNoEstoque(String codProduto, BigDecimal quantidade, MovementType movementType) {
        if (quantidade.compareTo(BigDecimal.valueOf(0)) <= 0) throw new IllegalArgumentException("Quantidade inválida");
        Optional<StockItem> itemEstoqueTeste = this.verificarEstoqueProduto(codProduto);
        if (itemEstoqueTeste.isPresent()) { //Se o produto já existir o produto no estoque
            StockItem stockItemExistente = itemEstoqueTeste.get();
            if (stockItemExistente.getProduct().getStatus().equals("INATIVO")) {
                throw new IllegalArgumentException("Não é possível remover produtos sem um cadastro ativo!");
            } else {
                BigDecimal quantidadeAntiga = stockItemExistente.getQuantidade();
                int validacaoSaldoQt = quantidadeAntiga.compareTo(quantidade);
                if (validacaoSaldoQt < 0)
                    throw new IllegalArgumentException("Não há produtos suficientes: " + quantidadeAntiga);
                else if (validacaoSaldoQt == 0) {
                    stockItemExistente.setQuantidade(BigDecimal.valueOf(0));
                    stockItemExistente.setAtivo(false);
                } else {
                    stockItemExistente.setQuantidade(quantidadeAntiga.add(quantidade.multiply(BigDecimal.valueOf(-1)))); // Realizando a subtração
                    itemEstoqueRepository.atualizarQuantidade(stockItemExistente.getQuantidade(), stockItemExistente.getId());
                    Movement mov = new Movement(null, movementType, Date.from(Instant.now()), stockItemExistente.getProduct(), quantidade);
                    movimentacaoRepository.save(mov);
                    this.sincronizarComBanco();
                }
                itemEstoqueRepository.atualizarQuantidade(stockItemExistente.getQuantidade(), stockItemExistente.getId());
                Movement mov = new Movement(null, movementType, Date.from(Instant.now()), stockItemExistente.getProduct(), quantidade);
                movimentacaoRepository.save(mov);
                this.sincronizarComBanco();
            }
        } else { // Caso não exista o produto no estoque
            throw new IllegalArgumentException("Produto não está no estoque!");
        }
    }


    public List<StockItem> mostrarTodosItens() {
        return itemEstoqueRepository.findAll();
    }


    public List<Movement> filtrarMovimentacoesData(Date inicio, Date fim){
        List<Movement> movimentacoes = movimentacaoRepository.findAll();
        List<Movement> movimentacoess = movimentacoes.stream()
                .filter(x -> (x.getDataMovimentacao().after(inicio) && x.getDataMovimentacao().before(fim))).collect(Collectors.toList());
        return movimentacoess;
    }

    public List<Movement> filtraSaidasVendas(Date inicio, Date fim){
        List<Movement> movimentacoes = movimentacaoRepository.findAll();
        List<Movement> movimentacoess = movimentacoes.stream()
                .filter(x -> (x.getDataMovimentacao().after(inicio) && x.getDataMovimentacao().before(fim))).collect(Collectors.toList())
                .stream().filter(x -> x.getTipo().equals(MovementType.VENDA)).collect(Collectors.toList());
        return movimentacoess;
    }

    private void sincronizarComBanco() {
        this.produtosArmazenados = itemEstoqueRepository.findAll();
        this.movimentacoesDoEstoque = movimentacaoRepository.findAll();
    }
}
