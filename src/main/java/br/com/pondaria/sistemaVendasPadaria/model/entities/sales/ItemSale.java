package br.com.pondaria.sistemaVendasPadaria.model.entities.sales;

import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_item_sale")
public class ItemSale {

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

    // Criação do item de venda
    public static ItemSale createItemSale(Product product, BigDecimal quantity, Sale sale){
        return ItemSale.builder()
                .fatherSale(sale)
                .quantity(quantity)
                .product(product)
                .totalvalue(product.getSaleValue().multiply(quantity))
                .build();
    }

    //Criar método para exibir item para venda

    //@JsonIgnore // Para evitar que ao puxar no json eu tenha redundância
    //método get da venda pai

    // método para exibir itemVenda no carrinho
}
