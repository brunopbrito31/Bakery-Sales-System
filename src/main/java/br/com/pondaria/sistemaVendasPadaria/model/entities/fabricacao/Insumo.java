package br.com.pondaria.sistemaVendasPadaria.model.entities.fabricacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_insumo")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_barras",nullable = false,unique = true)
    private String codigoBarras;

    @Column(name= "quantidade", nullable = false)
    private BigDecimal quantidade;

    @Column(name= "codigobarrasproudtopai",nullable = false)
    private String codigoBarrasProdutoPai;


}
