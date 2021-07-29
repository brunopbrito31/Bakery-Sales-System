package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.Estoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.Movimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class EstoqueService {

    private Estoque estoque;


    @Autowired
    public EstoqueService(Estoque estoque) {
        this.estoque = estoque;
    }

    //futuramente: estoquista

    public void adicionarAoEstoque(Produto produto, BigDecimal quantidade) throws IllegalArgumentException {
        estoque.movimentarItemNoEstoque(produto, quantidade, TipoMovimentacao.ENTRADA);
    }

    public void estornarAoEstoque(Produto produto, BigDecimal quantidade) {
        estoque.movimentarItemNoEstoque(produto, quantidade, TipoMovimentacao.ESTORNO);
    }

    public void darBaixaInternaNoEstoque(Produto produto, BigDecimal quantidade) {
        estoque.movimentarItemNoEstoque(produto, quantidade, TipoMovimentacao.BAIXAINTERNA);
    }

    public void retirarDoEstoqueVenda(Produto produto, BigDecimal quantidade) {
        estoque.movimentarItemNoEstoque(produto, quantidade, TipoMovimentacao.VENDA);
    }

    public List<Movimentacao> verificarMovPeriodo(Date inicio, Date fim) throws IllegalArgumentException {
        return estoque.verificarMovimentaçãoPeriodo(inicio, fim);
    }

    public ItemEstoque verificarEstoqueProduto(Long id) throws IllegalArgumentException {
        return estoque.verificarEstoqueProduto(id);
    }

    public List<ItemEstoque> mostrarTodosItens() throws IllegalArgumentException {
        return estoque.getProdutosArmazenados();
    }
}
