package br.com.pondaria.sistemaVendasPadaria.model.entities.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

// Não está puxando os getters e setters
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
