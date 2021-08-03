package br.com.pondaria.sistemaVendasPadaria.model.entities.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_homemade_products")
public class HomemadeProduct extends Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<Product> inputs;

    //método para exibir para cadastro

    //método para exibir para fabricação

    // buscar uma forma de especificar
    // quantos produtos de cada serão necessários, espécie de receita, através de método?

}
