package br.com.pondaria.sistemaVendasPadaria.model.entities.usuario;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@Entity(name = "tb_cliente")
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal saldoBonus;

    private String cpf;

    public void acrescentaBonus(BigDecimal valor) {
        if(saldoBonus == null) saldoBonus = BigDecimal.valueOf(0);
        this.saldoBonus.add(valor);
    }
}
