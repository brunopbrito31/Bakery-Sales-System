/*package br.com.pondaria.sistemaVendasPadaria.model.entities.deposito;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.MovimentacaoRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@OneToMany(mappedBy = "id")
    private List<ItemEstoque> itensEstoque;

    //pode ser uma venda ou baixa pelo padeiro
    public void retirarItem() {


    }
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
