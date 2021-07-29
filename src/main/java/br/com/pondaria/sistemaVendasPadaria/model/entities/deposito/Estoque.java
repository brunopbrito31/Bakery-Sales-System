package br.com.pondaria.sistemaVendasPadaria.model.entities.deposito;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.MovimentacaoRepository;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import java.math.BigDecimal;
import java.util.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@ManagedBean
public class Estoque {

    private ItemEstoqueRepository itemEstoqueRepository;

    private MovimentacaoRepository movimentacaoRepository;

    private List<ItemEstoque> produtosArmazenados;

    private List<Movimentacao> movimentacoesDoEstoque;

    @Autowired
    public Estoque(ItemEstoqueRepository itemEstoqueRepository, MovimentacaoRepository movimentacaoRepository, List<ItemEstoque> produtosArmazenados, List<Movimentacao> movimentacoesDoEstoque) {
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtosArmazenados = produtosArmazenados;
        this.movimentacoesDoEstoque = movimentacoesDoEstoque;
        atualizarDoBanco();
    }

    public void atualizarDoBanco(){
        this.produtosArmazenados = itemEstoqueRepository.findAll();
        this.movimentacoesDoEstoque = movimentacaoRepository.findAll();
    }


    public ItemEstoque verificarEstoqueProduto(Long id) {
        Optional<ItemEstoque> itemEstoqueSelecionado = this.produtosArmazenados.stream()
                .filter(x -> x.getProduto().getId().equals(id)).findFirst();
        if (!itemEstoqueSelecionado.isPresent()) {
            throw new IllegalArgumentException("Não há estoque do produto escolhido");
        } else {
            return itemEstoqueSelecionado.get();
        }
    }

    public List<Movimentacao> verificarMovimentaçãoPeriodo(Date inicioPeriodo, Date fimPeriodo) {
        // adicionar trecho de código para incluir as datas limite
        List<Movimentacao> movimentacoesPeriodo = this.movimentacoesDoEstoque.stream()
                .filter(x -> (x.getDataMovimentacao().before(fimPeriodo) && x.getDataMovimentacao().after(inicioPeriodo)))
                .collect(Collectors.toList());

        if (movimentacoesPeriodo.isEmpty()) {
            throw new IllegalArgumentException("Não há movimentações no periodo selecionado");
        } else {
            return movimentacoesPeriodo;
        }
    }

    public void movimentarItemNoEstoque(Produto produto, BigDecimal quantidade, TipoMovimentacao tipoMovimentacao) {
        // já existe, atualizar o item estoque existente ai , mudar o método de verificar para buscar pelo código de barras
        Movimentacao mov;
        Long idItemEstoque = itemEstoqueRepository.verificarPorCod(produto.getCodigoBarras()); // verifica se já há um item de estoque do produto cadastrado no banco de dados
        if (idItemEstoque != null) { // Condição para idItemEstoque existente
            ItemEstoque itemEstoqueExistente = itemEstoqueRepository.findById(idItemEstoque).get();
            BigDecimal qtAntiga = itemEstoqueExistente.getQuantidade();
            if (tipoMovimentacao.equals(TipoMovimentacao.ENTRADA) || tipoMovimentacao.equals(TipoMovimentacao.ESTORNO)) {
                itemEstoqueRepository.atualizarQuantidade(qtAntiga.add(quantidade), itemEstoqueExistente.getId());
                if (itemEstoqueExistente.getAtivo() == false) itemEstoqueExistente.setAtivo(true);
                this.produtosArmazenados = itemEstoqueRepository.findAll();
            } else {// SAIDA o VENDA
                if (qtAntiga.compareTo(quantidade) < 0)
                    throw new IllegalArgumentException("Você não possui " + produto.getDescricao() + " suficiente no estoque!");
                else
                    itemEstoqueRepository.atualizarQuantidade(qtAntiga.add((BigDecimal.valueOf(-1d).multiply(quantidade)).add(qtAntiga)), itemEstoqueExistente.getId());
                this.produtosArmazenados = itemEstoqueRepository.findAll();
            }
        } else { // Caso não haja item de estoque do produto cadastrado
            if (tipoMovimentacao.equals(TipoMovimentacao.ENTRADA) || tipoMovimentacao.equals(TipoMovimentacao.ESTORNO)) {
                ItemEstoque itemNovo = new ItemEstoque();
                itemNovo.setAtivo(true);
                itemNovo.setProduto(produto);
                itemNovo.setQuantidade(quantidade);
                itemEstoqueRepository.save(itemNovo); // persistindo no banco
                this.produtosArmazenados = itemEstoqueRepository.findAll(); // atualizando a instância
            } else {
                throw new IllegalArgumentException("Não há o produto no estoque!");
            }
        }
        mov = Movimentacao.builder().dataMovimentacao(Date.from(Instant.now())) // solução alternativa, tipo de data = LocalDate
                .quantidade(quantidade).produtoMovimentado(produto)
                .tipo(tipoMovimentacao).build();
        movimentacaoRepository.save(mov); // registro da movimentação no banco de dados
        this.movimentacoesDoEstoque = movimentacaoRepository.findAll();
    }


}
/*
    //OBS: A lista mantém a ordem de entrada
    public double retirada(Produto produto, double quantidade) {
        double totalNecessario = quantidade;
        double totalItens = 0;
        for (Lote lote : lotes) {
            if (lote.getProduto().equals(produto)){
                if (lote.getQuantidade() >= totalNecessario) {
                    lote.setQuantidade(lote.getQuantidade() - totalNecessario);
                    totalItens = totalNecessario;
                    break;
                } else {
                    totalItens += lote.getQuantidade();
                    totalNecessario -= lote.getQuantidade();
                }
                //Remove o lote zerado
                if (lote.getQuantidade() == 0) {
                    lotes.remove(lote);
                }
            }
        }
        return totalItens;
    }

     implementar operação que faz busca no banco de dados
    public boolean verificaDisponibilidade (Produto produto, double quantidade) {
        double totalItens = 0;
        for (Lote lote : lotes) {
            if (lote.getProduto().equals(produto)) {
                totalItens += lote.getQuantidade();
            }
        }
        return totalItens >= quantidade;
        return true;
    }

    public double addMovimentacaoRetirada(Produto produto, double quantidade) {
        //Quando a venda ou a requisição de produtos é finalizada
        double totalRetiradoEstoque = retirada(produto, quantidade);
        movimentacoes.add(new Movimentacao(EstadoDoPedido.VENDIDO, LocalDateTime.now(), produto, totalRetiradoEstoque));
        return totalRetiradoEstoque;
    }

    public boolean addMovimentacaoVerificacao(Produto produto, double quantidade) {
        //Verifica se tem o produto e adiciona no carrinho somente sabendo se há disponibilidade
        //OBS: Ainda não faz a transação
        if(verificaDisponibilidade(produto, quantidade)) {
            movimentacoes.add(new Movimentacao(EstadoDoPedido.ADICIONADO, LocalDateTime.now(), produto, quantidade));
            return true;
        } else {
            return false;
        }
    }

    public void addMovimentacaoRetorno(Produto produto, double quantidade) {
        //Caso haja devoluções
        movimentacoes.add(new Movimentacao(EstadoDoPedido.ESTORNADO, LocalDateTime.now(), produto, quantidade));
    }

    public void listarMovimentacoes() {
        for (Movimentacao movimentacao : movimentacoes) {
            System.out.println(movimentacao);
        }
    }

    public Double transfereProduto(Produto produto, double quantidade) {
        if (addMovimentacaoVerificacao(produto, quantidade)) {
            return addMovimentacaoRetirada(produto, quantidade);
        } else {
            System.out.println("Quantidade insuficiente ou nula");
            return null;
        }
    }

*/