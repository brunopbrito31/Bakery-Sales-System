package br.com.pondaria.sistemaVendasPadaria.model.entities.users;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@Entity(name = "tb_clients")
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bonus_balance")
    private BigDecimal bonusBalance;

    @Column(name = "cpf")
    private String cpf;

    public void addBonus(BigDecimal bonusValue) {
        if(bonusBalance == null) bonusBalance = BigDecimal.valueOf(0);
        this.bonusBalance.add(bonusValue);
    }
}
