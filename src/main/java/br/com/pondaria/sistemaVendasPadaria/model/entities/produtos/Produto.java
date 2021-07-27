package br.com.pondaria.sistemaVendasPadaria.model.entities.produtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valorCusto;

    @Column
    private BigDecimal pesoUnitario;

    @Column(nullable = false)
    private String unidadeMedida;

    @Column(nullable = false, unique = true)
    private String codigoBarras;

    @Column(nullable = false)
    private BigDecimal valorDeVenda;

    private Boolean produtoFabricado;


    // verificar lógica para ativar a lista de insumos e relacionar com o id de produto
    //private List<String> CodInsumos; // lógica para instanciar a lista

    public String exibirParaVenda(){
        return "Código de Barras: "+getCodigoBarras()+"\n"+
        "Descrição: "+getDescricao()+"\n"+
                "Vendido por: "+getUnidadeMedida()+"\n"+
                "Valor por "+getPesoUnitario()+" R$:"+getValorDeVenda()+"\n";
    }


}
