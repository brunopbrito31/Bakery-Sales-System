package br.com.pondaria.sistemaVendasPadaria.model.entities.vendas;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity(name = "tb_venda")
public class Venda {

    private Venda(Long id, List<ItemVenda> itensVenda, BigDecimal valorTotal, Date dataHora) {
        this.id = id;
        this.itensVenda = itensVenda;
        this.valorTotal = valorTotal;
        this.dataHora = dataHora;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    //sintaxe para relacionar os ids
    private List<ItemVenda> itensVenda;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private Date dataHora;

    // incrementar e finalizar através do service
    public static Venda AbrirVenda(String senha){
        //Inserir validação com senha de caixa.
        Venda vendaIniciada = new Venda(null, new ArrayList<>(),BigDecimal.valueOf(0d),Date.from(Instant.now())); // verificar a possibilidade de trocar para LocalDateTime
        return vendaIniciada;
    }

    // corrigir método
    /*public void exibir(){
        this.getItensVenda().stream().map(x -> x.exibirItemVenda()).collect(Collectors.toList()).forEach(System.out::println);
    }*/

    public void atualizarValorTotal(ItemVenda itemVenda){
        valorTotal = valorTotal.add(itemVenda.getVlrTotal());
    }
}
