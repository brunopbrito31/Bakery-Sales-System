package br.com.pondaria.sistemaVendasPadaria.model.entities.products;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.ProductStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Data @Builder
@Entity(name = "tb_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "costvalue", nullable = false)
    private BigDecimal costValue;

    @Column(name = "unitweight", nullable = false)
    private BigDecimal unitWeight;

    @Column(name = "unitmeasure", nullable = false)
    private String unitMeasure;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(name = "salevalue", nullable = false)
    private BigDecimal saleValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @Autowired
    public Product(Long id, String description, BigDecimal costValue, BigDecimal unitWeight, String unitMeasure, String barcode, BigDecimal saleValue, ProductStatus status) {
        this.id = id;
        this.description = description;
        this.costValue = costValue;
        this.unitWeight = unitWeight;
        this.unitMeasure = unitMeasure;
        this.barcode = barcode;
        this.saleValue = saleValue;
        this.status = status;
    }

    // método para exibir o produto para venda

    // método para exibir o cadastro do produto

}
