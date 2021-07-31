package br.com.pondaria.sistemaVendasPadaria.model.entities.vendas;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_item_venda")
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Produto produto;

    @Column(nullable = false)
    private BigDecimal quantidade;

    //Criar lógica para calcular o valor total automaticamente
    @Column(nullable = false)
    private BigDecimal vlrTotal;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda vendaPai; // fazer um mapeamento com o id de venda

    public String exibirItemVenda(){
        return produto.exibirParaVenda()+"Quantidade: "+quantidade+"\n"+
                "Valor Total R$: "+vlrTotal+"\n";
    }

    @JsonIgnore // Para evitar que ao puxar no json eu tenha redundância
    public Venda getVendaPai(){
        return this.vendaPai;
    }

    public String exibirParaVenda(){
        return "Descricao: "+produto.getDescricao()+"\n"+
                "Quantidade: "+quantidade+"\n"+
                "Valor R$: "+vlrTotal+"\n";
    }

}
