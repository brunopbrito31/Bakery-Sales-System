package br.com.pondaria.sistemaVendasPadaria.model.entities.produtos;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
@Entity(name = "tb_products")
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal costValue;

    @Column(nullable = false)
    private BigDecimal unitWeight;

    @Column(nullable = false)
    private String unitMeasure;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(nullable = false)
    private BigDecimal saleValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    // método para exibir o produto para venda

    // método para exibir o cadastro do produto

}
