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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
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
    }

    // ok - Testar
    public Optional<ItemEstoque> verificarEstoqueProduto(String codBarras) {
        Long idProduto =itemEstoqueRepository.verificarPorCod(codBarras);
        Optional<ItemEstoque> itemEstoqueSelecionado = itemEstoqueRepository.findById(idProduto);
        if(!itemEstoqueSelecionado.isPresent()) return null;
        return itemEstoqueSelecionado;
    }

    // Comporta a situação de estorno e de adição
    public void adicionarItemNoEstoque(String codProduto, BigDecimal quantidade, TipoMovimentacao tipoMovimentacao){
        Optional<ItemEstoque> itemEstoqueTeste = this.verificarEstoqueProduto(codProduto);
        if(itemEstoqueTeste.isPresent()){ //Se o produto já existir o produto no estoque
            ItemEstoque itemEstoqueExistente = itemEstoqueTeste.get();
            if(itemEstoqueExistente.getProduto().getStatus().equals("INATIVO")){
                throw new IllegalArgumentException("Não é possível adicionar produtos sem um cadastro ativo!");
            }else{
                BigDecimal quantidadeAntiga = itemEstoqueExistente.getQuantidade();
                if(quantidadeAntiga.equals(0)) itemEstoqueExistente.setAtivo(true);
                itemEstoqueExistente.setQuantidade(quantidadeAntiga.add(quantidade));
                itemEstoqueRepository.atualizarQuantidade(itemEstoqueExistente.getQuantidade(),itemEstoqueExistente.getId());
                Movimentacao mov = new Movimentacao(null,tipoMovimentacao,Date.from(Instant.now()), itemEstoqueExistente.getProduto(),quantidade);
                movimentacaoRepository.save(mov);
                this.sincronizarComBanco();
            }
        }else{ // Caso não exista o produto no estoque
            Optional<Produto> produtoTeste = produtoRepository.buscarPeloCodigoBarras(codProduto);
            if(!produtoTeste.isPresent()) throw new IllegalArgumentException("Produto não está cadastrado");
            else if(produtoTeste.get().getStatus().equals("INATIVO")) throw new IllegalArgumentException ("Não é possível adicionar produtos em um cadastro ativo!");
            else{
                Produto produto = produtoRepository.buscarPeloCodigoBarras(codProduto).get();
                ItemEstoque itemEstoqueNovo = new ItemEstoque(null,produto,quantidade,true);
                itemEstoqueRepository.save(itemEstoqueNovo);
                Movimentacao mov = new Movimentacao(null,tipoMovimentacao,Date.from(Instant.now()),itemEstoqueNovo.getProduto(),quantidade);
                movimentacaoRepository.save(mov);
                this.sincronizarComBanco();
            }
        }
    }

    //Comporta a situação de saida para venda e baixa interna
    public void retirarItemNoEstoque(String codProduto, BigDecimal quantidade, TipoMovimentacao tipoMovimentacao){
        Optional<ItemEstoque> itemEstoqueTeste = this.verificarEstoqueProduto(codProduto);
        if(itemEstoqueTeste.isPresent()){ //Se o produto já existir o produto no estoque
            ItemEstoque itemEstoqueExistente = itemEstoqueTeste.get();
            if(itemEstoqueExistente.getProduto().getStatus().equals("INATIVO")){
                throw new IllegalArgumentException("Não é possível remover produtos sem um cadastro ativo!");
            }else{
                BigDecimal quantidadeAntiga = itemEstoqueExistente.getQuantidade();
                Integer validacaoSaldoQt = quantidadeAntiga.compareTo(quantidade);
                if(validacaoSaldoQt < 0) throw new IllegalArgumentException("Não há produtos suficientes: "+quantidadeAntiga);
                else if(validacaoSaldoQt == 0){
                    itemEstoqueExistente.setAtivo(false);
                }else{
                    itemEstoqueExistente.setQuantidade(quantidadeAntiga.add(quantidade.multiply(BigDecimal.valueOf(-1)))); // Realizando a subtração
                    itemEstoqueRepository.atualizarQuantidade(itemEstoqueExistente.getQuantidade(),itemEstoqueExistente.getId());
                    Movimentacao mov = new Movimentacao(null,tipoMovimentacao,Date.from(Instant.now()), itemEstoqueExistente.getProduto(),quantidade);
                    movimentacaoRepository.save(mov);
                    this.sincronizarComBanco();
                }
                itemEstoqueRepository.atualizarQuantidade(itemEstoqueExistente.getQuantidade(),itemEstoqueExistente.getId());
                Movimentacao mov = new Movimentacao(null,tipoMovimentacao,Date.from(Instant.now()), itemEstoqueExistente.getProduto(),quantidade);
                movimentacaoRepository.save(mov);
                this.sincronizarComBanco();
            }
        }else{ // Caso não exista o produto no estoque
            throw new IllegalArgumentException("Produto não está no estoque!");
        }
    }


    public List<ItemEstoque> mostrarTodosItens() {
        return itemEstoqueRepository.findAll();
    }


    /*public List<Movimentacao> verificarMovPeriodo(Date inicio, Date fim) throws IllegalArgumentException {
        return estoque.verificarMovimentaçãoPeriodo(inicio, fim);
    }*/

    private void sincronizarComBanco(){
        this.produtosArmazenados = itemEstoqueRepository.findAll();
        this.movimentacoesDoEstoque = movimentacaoRepository.findAll();
    }
}
