/*package br.com.pondaria.sistemaVendasPadaria.model.entities.fabricacao;

import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@Component
public class Fabricacao {

    private Produto produto;

    //código de barras do produto que é usado como insumo, quantidade
    private Map<String, BigDecimal> insumos;

    @Autowired
    public Fabricacao(Produto produto, Map<String, BigDecimal> insumos) {
        this.produto = produto;
        this.insumos = insumos;
    }

}
*/