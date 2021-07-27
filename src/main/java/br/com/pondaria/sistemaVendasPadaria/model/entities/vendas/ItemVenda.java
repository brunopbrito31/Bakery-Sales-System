package br.com.pondaria.sistemaVendasPadaria.model.entities.vendas;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;

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
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal vlrTotal;

    //private Venda vendaPai; // fazer um mapeamento com o id de venda

    public String exibirItemVenda(){
        return produto.exibirParaVenda()+"Quantidade: "+quantidade+"\n"+
                "Valor Total R$: "+vlrTotal+"\n";
    }

}
