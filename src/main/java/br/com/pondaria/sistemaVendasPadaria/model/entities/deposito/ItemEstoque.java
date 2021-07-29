package br.com.pondaria.sistemaVendasPadaria.model.entities.deposito;

import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_estoque")
public class ItemEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Produto produto;

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(nullable = false)
    private Boolean ativo;

    /*@ManyToOne
    @JoinColumn(name = "estoque_id") // Verificar se esse mapeamento sobe
    private Estoque estoque;*/

    // método para verificar o valor e atualizar o status do item no estoque
    public void atualizarStatus(){
        if(quantidade.equals(BigDecimal.valueOf(0))) this.ativo = false;
        else this.ativo = true;
    }
}
