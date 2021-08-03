package br.com.pondaria.sistemaVendasPadaria.model.entities.sales;

import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
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
@Entity(name = "tb_item_sale")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private BigDecimal totalvalue;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale fatherSale;

    public static SaleItem createItemSale(Product product, BigDecimal quantity, Sale sale) {
        return SaleItem.builder()
                .fatherSale(sale)
                .quantity(inputBigDecimalValidator(quantity))
                .product(product)
                .totalvalue(product.getSaleValue().multiply(quantity))
                .build();
    }

    public String itemSaleDetail(){
        StringBuilder sb = new StringBuilder();
        sb.append("Description: "+product.getDescription()).append("\n")
                .append("Value $: "+product.getSaleValue()+" per "+product.getUnitMeasure()).append("\n")
                .append("Quantity in sale: "+this.getQuantity()+" Total Value $: "+this.getTotalvalue()).append("\n");
        return sb.toString();
    }

    @JsonIgnore
    public Sale getFatherSale() {
        return fatherSale;
    }

    private static BigDecimal inputBigDecimalValidator(BigDecimal num) {
        if (num.compareTo(BigDecimal.valueOf(0)) <= 0) throw new IllegalArgumentException("Invalid input");
        else return num;
    }
}
