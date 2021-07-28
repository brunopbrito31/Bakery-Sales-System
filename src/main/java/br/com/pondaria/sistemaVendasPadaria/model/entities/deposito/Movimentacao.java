package br.com.pondaria.sistemaVendasPadaria.model.entities.deposito;


import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(name = "data", nullable = false)
    private Date dataMovimentacao; // Verificar se há necessidade de troca para LocalDateTime

    @OneToOne
    private Produto produtoMovimentado;

    @Column(nullable = false)
    private BigDecimal quantidade;

}
