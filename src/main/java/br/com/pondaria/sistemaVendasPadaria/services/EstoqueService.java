package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.Movimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.MovimentacaoRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Calendar;
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

    private List<ItemEstoque> produtosArmazenados;

    private List<Movimentacao> movimentacoesDoEstoque;


    @Autowired
    public EstoqueService(ItemEstoqueRepository itemEstoqueRepository, MovimentacaoRepository movimentacaoRepository, ProdutoRepository produtoRepository, List<ItemEstoque> produtosArmazenados, List<Movimentacao> movimentacoesDoEstoque) {
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.produtosArmazenados = produtosArmazenados;
        this.movimentacoesDoEstoque = movimentacoesDoEstoque;
        sincronizarComBanco();
    }

    // ok - Testar
    public Optional<ItemEstoque> verificarEstoqueProduto(String codBarras) {
        Optional<ItemEstoque> itemEstoqueSelecionado = itemEstoqueRepository.buscarItemEstoquePeloCodProduto(codBarras);
        if (!itemEstoqueSelecionado.isPresent()) return null; // pensar na lógica de criar um estoque
        return itemEstoqueSelecionado;
    }

    public void adicionarItemNoEstoque(String codProduto, BigDecimal quantidade, TipoMovimentacao tipoMovimentacao) {
        if (quantidade.compareTo(BigDecimal.valueOf(0)) <= 0) throw new IllegalArgumentException("Quantidade inválida");
        Produto produtoTeste = produtoRepository.buscarPeloCodigoBarras(codProduto).get(); // Pegar o produto que será movimentado
        Movimentacao movimentacao;
        if (produtoTeste.getStatus().equals("INATIVO"))
            throw new IllegalArgumentException("Não pode manipular um produto que está com o cadastro desativado");
        Optional<ItemEstoque> itemTeste = itemEstoqueRepository.buscarItemEstoquePeloCodProduto(codProduto);
        if (itemTeste.isPresent()) { // Caso já haja um item de estoque com o produto escolhido
            BigDecimal qtdAntiga = itemTeste.get().getQuantidade();
            itemTeste.get().setQuantidade(qtdAntiga.add(quantidade));
            itemTeste.get().setAtivo(true);
            itemEstoqueRepository.atualizarQuantidade(itemTeste.get().getQuantidade(), itemTeste.get().getId());
            movimentacao = Movimentacao.builder()
                    .dataMovimentacao(Date.from(Instant.now()))
                    .produtoMovimentado(produtoTeste)
                    .quantidade(quantidade)
                    .tipo(tipoMovimentacao)
                    .build();
        } else { // ------------------  Caso não haja um item de estoque com o produto escolhido
            ItemEstoque itemEstoqueNovo = ItemEstoque.builder()
                    .quantidade(quantidade)
                    .produto(produtoTeste)
                    .ativo(true).build();
            itemEstoqueRepository.save(itemEstoqueNovo);
            movimentacao = Movimentacao.builder()
                    .dataMovimentacao(Date.from(Instant.now()))
                    .produtoMovimentado(produtoTeste)
                    .quantidade(quantidade)
                    .tipo(tipoMovimentacao)
                    .build();
        }
        movimentacaoRepository.save(movimentacao);
        sincronizarComBanco();
    }

    public void retirarItemNoEstoque(String codProduto, BigDecimal quantidade, TipoMovimentacao tipoMovimentacao) {
        if (quantidade.compareTo(BigDecimal.valueOf(0)) <= 0) throw new IllegalArgumentException("Quantidade inválida");
        Optional<ItemEstoque> itemEstoqueTeste = this.verificarEstoqueProduto(codProduto);
        if (itemEstoqueTeste.isPresent()) { //Se o produto já existir o produto no estoque
            ItemEstoque itemEstoqueExistente = itemEstoqueTeste.get();
            if (itemEstoqueExistente.getProduto().getStatus().equals("INATIVO")) {
                throw new IllegalArgumentException("Não é possível remover produtos sem um cadastro ativo!");
            } else {
                BigDecimal quantidadeAntiga = itemEstoqueExistente.getQuantidade();
                int validacaoSaldoQt = quantidadeAntiga.compareTo(quantidade);
                if (validacaoSaldoQt < 0)
                    throw new IllegalArgumentException("Não há produtos suficientes: " + quantidadeAntiga);
                else if (validacaoSaldoQt == 0) {
                    itemEstoqueExistente.setQuantidade(BigDecimal.valueOf(0));
                    itemEstoqueExistente.setAtivo(false);
                } else {
                    itemEstoqueExistente.setQuantidade(quantidadeAntiga.add(quantidade.multiply(BigDecimal.valueOf(-1)))); // Realizando a subtração
                    itemEstoqueRepository.atualizarQuantidade(itemEstoqueExistente.getQuantidade(), itemEstoqueExistente.getId());
                    Movimentacao mov = new Movimentacao(null, tipoMovimentacao, Date.from(Instant.now()), itemEstoqueExistente.getProduto(), quantidade);
                    movimentacaoRepository.save(mov);
                    this.sincronizarComBanco();
                }
                itemEstoqueRepository.atualizarQuantidade(itemEstoqueExistente.getQuantidade(), itemEstoqueExistente.getId());
                Movimentacao mov = new Movimentacao(null, tipoMovimentacao, Date.from(Instant.now()), itemEstoqueExistente.getProduto(), quantidade);
                movimentacaoRepository.save(mov);
                this.sincronizarComBanco();
            }
        } else { // Caso não exista o produto no estoque
            throw new IllegalArgumentException("Produto não está no estoque!");
        }
    }


    public List<ItemEstoque> mostrarTodosItens() {
        return itemEstoqueRepository.findAll();
    }


    public List<Movimentacao> filtrarMovimentacoesData(Date inicio, Date fim){
        List<Movimentacao> movimentacoes = movimentacaoRepository.findAll();
        List<Movimentacao> movimentacoess = movimentacoes.stream()
                .filter(x -> (x.getDataMovimentacao().after(inicio) && x.getDataMovimentacao().before(fim))).collect(Collectors.toList());
        return movimentacoess;
    }

    public List<Movimentacao> filtraSaidasVendas(Date inicio, Date fim){
        List<Movimentacao> movimentacoes = movimentacaoRepository.findAll();
        List<Movimentacao> movimentacoess = movimentacoes.stream()
                .filter(x -> (x.getDataMovimentacao().after(inicio) && x.getDataMovimentacao().before(fim))).collect(Collectors.toList())
                .stream().filter(x -> x.getTipo().equals(TipoMovimentacao.VENDA)).collect(Collectors.toList());
        return movimentacoess;
    }

    private void sincronizarComBanco() {
        this.produtosArmazenados = itemEstoqueRepository.findAll();
        this.movimentacoesDoEstoque = movimentacaoRepository.findAll();
    }
}
