package br.com.pondaria.sistemaVendasPadaria.model.entities.deposit;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.ProductStatus;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_stock")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status",nullable = false)
    private ProductStatus productStatus; // aqui representa se o estoque possui esse tipo de produto ativo lá, enquanto nop produto serve para indicar o status do cadastro

    // método para verificar o valor e atualizar o status do item no estoque
    public void statusInStockUpdate(){
        // implmentar método para atualizkar o status do item no estoque
    }

    //método para adicionar ao estoque

    //método para retirar do estoque - verificar a relação com o banco de dados
}
