package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.Movimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import br.com.pondaria.sistemaVendasPadaria.services.EstoqueService;
import br.com.pondaria.sistemaVendasPadaria.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private EstoqueService estoqueService;
    private ProdutoService produtoService;
    private ProdutoRepository produtoRepository; // passsar para a camada de service depois

    @Autowired
    EstoqueController(EstoqueService estoqueService, ProdutoRepository produtoRepository, ProdutoService produtoService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
        this.produtoRepository = produtoRepository;
    }

    //Feito - ok
    @GetMapping("/exibirTodo")
    public List<ItemEstoque> mostrarEstoque() {
        try {
            return estoqueService.mostrarTodosItens();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Feito - ok
    @PostMapping("/adicionar")
    public MessageDTO adicionarItem(@RequestBody String[] entrada) {
        String codBarras = entrada[0];
        BigDecimal quantidade = BigDecimal.valueOf(Double.valueOf(entrada[1]));
        System.out.println("Valor de produto: "+codBarras);
        System.out.println("Valor de quantidade: "+quantidade);

        Produto produto = produtoService.buscarProdutoCodBarras(codBarras);

        estoqueService.adicionarAoEstoque(produto, quantidade);
        return MessageDTO.builder().msg("Produto adicionado com sucesso!").build();

        /*try {

        } catch (IllegalArgumentException e) {
            return MessageDTO.builder().msg("Error: " + e.getMessage()).build();
        }*/
    }

    // Feito
    @GetMapping("/exibir/{id}")
    public ItemEstoque exibirItemEstoque(@PathVariable Long id) {
        try {
            return estoqueService.verificarEstoqueProduto(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    //Feito -- Testar depois
    @GetMapping("/movimentacao/{d1}to{d2}")
    public List<Movimentacao> verificarMovPeriodo(@PathVariable Date d1, @PathVariable Date d2) {
        try {
            return estoqueService.verificarMovPeriodo(d1, d2);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
