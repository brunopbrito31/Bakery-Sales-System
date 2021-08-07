package br.com.pondaria.sistemaVendasPadaria.model.entities.deposit;


import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
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
@Entity(name = "tb_movement")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MovementType type;

    @Column(name = "data", nullable = false)
    private Date movementDate; // Verificar se há necessidade de troca para LocalDateTime

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product movementedProduct;

    @Column(nullable = false)
    private BigDecimal quantity;

}
