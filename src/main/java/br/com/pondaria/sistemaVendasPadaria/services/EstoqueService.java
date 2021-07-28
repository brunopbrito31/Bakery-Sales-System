package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.Estoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.Movimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;

@Service
public class EstoqueService {

    private MovimentacaoRepository movimentacaoRepository;

    private ItemEstoqueRepository itemEstoqueRepository;

    //private Estoque estoque;

    @Autowired
    public EstoqueService(MovimentacaoRepository movimentacaoRepository, ItemEstoqueRepository itemEstoqueRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        //this.estoque = estoque;
    }


    // Falta registrar tudo no estoque.
    public void movimentarItemNoEstoque(Produto produto, BigDecimal quantidade, TipoMovimentacao tipoMovimentacao) {
        // colocar o estoque aqui
        // já existe, atualizar o item estoque existente ai , mudar o método de verificar para buscar pelo código de barras
        Movimentacao mov;
        Long idItemEstoque = itemEstoqueRepository.verificar(produto.getId()); // verifica se já há um item de estoque do produto cadastrado no banco de dados
        if (idItemEstoque != null) { // Condição para idItemEstoque existente
            ItemEstoque itemEstoqueExistente = itemEstoqueRepository.findById(idItemEstoque).get();
            BigDecimal qtAntiga = itemEstoqueExistente.getQuantidade();
            if(tipoMovimentacao.equals(TipoMovimentacao.ENTRADA) || tipoMovimentacao.equals(TipoMovimentacao.ESTORNO)){
                itemEstoqueRepository.atualizarQuantidade(qtAntiga.add(quantidade), itemEstoqueExistente.getId());
                if (itemEstoqueExistente.getAtivo() == false) itemEstoqueExistente.setAtivo(true);
            }else{
                if(qtAntiga.compareTo(quantidade) < 0) throw new IllegalArgumentException("Você não possui "+produto.getDescricao()+" suficiente no estoque!");
                else itemEstoqueRepository.atualizarQuantidade(qtAntiga.add((BigDecimal.valueOf(-1d).multiply(quantidade)).add(qtAntiga)),itemEstoqueExistente.getId());
            }

        } else { // Caso não haja item de estoque do produto cadastrado
            if(tipoMovimentacao.equals(TipoMovimentacao.ENTRADA) || tipoMovimentacao.equals(TipoMovimentacao.ESTORNO)){
                ItemEstoque itemNovo = new ItemEstoque();
                itemNovo.setAtivo(true);
                itemNovo.setProduto(produto);
                itemNovo.setQuantidade(quantidade);
                //itensEstoque.add(itemNovo); verificar se há a necessidade, redundância
            }else{
                throw new IllegalArgumentException("Não há o produto no estoque!");
            }
        }

        mov = Movimentacao.builder().dataMovimentacao(Date.from(Instant.now()))
                .quantidade(quantidade).produtoMovimentado(produto)
                .tipo(tipoMovimentacao).build();
        movimentacaoRepository.save(mov); // registro da movimentação no banco de dados
    }



}
