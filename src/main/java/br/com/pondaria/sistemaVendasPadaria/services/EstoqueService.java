package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.Movimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.MovimentacaoRepository;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    private MovimentacaoRepository movimentacaoRepository;

    private ItemEstoqueRepository itemEstoqueRepository;

    // Representa o estoque puxando todos os itens do estoque
    private List<ItemEstoque>estoque;

    public List<ItemEstoque>getEstoque(){
        return estoque;
    }


    @Autowired
    public EstoqueService(MovimentacaoRepository movimentacaoRepository, ItemEstoqueRepository itemEstoqueRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        //this.estoque = itemEstoqueRepository.findAll();
    }


    public void instanciarEstoque(){
        estoque = itemEstoqueRepository.findAll();
    }

    public ItemEstoque buscarPorCod(String codBarras){
        Long id = itemEstoqueRepository.verificarPorCod(codBarras);
        Optional<ItemEstoque> itemEstoque = itemEstoqueRepository.findById(id);
        if(!itemEstoque.isPresent()){
            throw new IllegalArgumentException("Item de estoque não disponível ou não cadastrado");
        }
        return itemEstoque.get();
    }



    public void movimentarItemNoEstoque(Produto produto, BigDecimal quantidade, TipoMovimentacao tipoMovimentacao) {
        // já existe, atualizar o item estoque existente ai , mudar o método de verificar para buscar pelo código de barras
        Movimentacao mov;
        Long idItemEstoque = itemEstoqueRepository.verificarPorCod(produto.getCodigoBarras()); // verifica se já há um item de estoque do produto cadastrado no banco de dados
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
                itemEstoqueRepository.save(itemNovo);
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
