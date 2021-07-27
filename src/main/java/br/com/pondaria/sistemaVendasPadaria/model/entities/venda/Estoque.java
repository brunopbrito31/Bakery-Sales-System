package br.com.pondaria.sistemaVendasPadaria.model.entities.venda;

import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.enums.EstadoDoPedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Estoque {

    //Atributos
    //Atributos relação
    private List<Lote> lotes;
    private List<Movimentacao> movimentacoes;

    //Construtor
    public Estoque() {
        this.lotes = new ArrayList<>();
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    public List<Movimentacao> getMovimentacoes(){
        return movimentacoes;
    }

    //Métodos da classe
    public void entrada(Lote lote) {
        this.lotes.add(lote);
    }

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

    public boolean verificaDisponibilidade (Produto produto, double quantidade) {
        double totalItens = 0;
        for (Lote lote : lotes) {
            if (lote.getProduto().equals(produto)) {
                totalItens += lote.getQuantidade();
            }
        }
        return totalItens >= quantidade;
    }


    public void listarLotes() {
        for (Lote lote : lotes) {
            System.out.println(lote);
        }
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

}
