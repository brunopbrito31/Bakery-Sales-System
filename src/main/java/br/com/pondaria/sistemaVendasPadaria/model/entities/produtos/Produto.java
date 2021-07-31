package br.com.pondaria.sistemaVendasPadaria.model.entities.produtos;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.StatusProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
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

    @Column(nullable = false)
    private Boolean produtoFabricado;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusProduto status;

    public String exibirParaVenda(){
        return "Código de Barras: "+getCodigoBarras()+"\n"+
        "Descrição: "+getDescricao()+"\n"+
                "Vendido por: "+getUnidadeMedida()+"\n"+
                "Valor por "+getPesoUnitario()+" R$:"+getValorDeVenda()+"\n";
    }

    public String exibirCadastro(){
        String exibicao=
                "Id: "+id+"\n"+
                "Descrição: "+descricao+"\n"+
                "Valor de Custo R$: "+valorCusto+"\n"+
                "Peso Unitário: "+pesoUnitario+" "+unidadeMedida+"\n"+
                "Código de Barras: "+codigoBarras+"\n"+
                "Valor de Venda R$: "+valorDeVenda+"\n"+
                "Status do Cadastro: "+status;
                if(produtoFabricado) exibicao = exibicao.concat("+\n"+"Produto Caseiro");

                return exibicao;
    }


}
