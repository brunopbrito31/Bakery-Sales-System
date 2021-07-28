package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import br.com.pondaria.sistemaVendasPadaria.services.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private EstoqueService estoqueService;
    private ProdutoRepository produtoRepository; // passsar para a camada de service depois

    @Autowired
    EstoqueController(EstoqueService estoqueService,ProdutoRepository produtoRepository) {
        this.estoqueService = estoqueService;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/exibir")
    public List<ItemEstoque> mostrarEstoque() {
        estoqueService.instanciarEstoque();
        return estoqueService.getEstoque();
    }

    @PostMapping("/adicionar")
    public MessageDTO adicionarItem(@RequestBody String codBarras, @RequestBody BigDecimal quantidade){
        Long id =  produtoRepository.buscarProdutoPeloCodigo(codBarras);
        Produto produto = produtoRepository.findById(id).get(); // tratar a exceção
        estoqueService.movimentarItemNoEstoque(produto,quantidade,TipoMovimentacao.ENTRADA);
        return MessageDTO.builder().msg("Produto adicionado com sucesso").build();
    }

    @GetMapping("/exibir/{codBarras}")
    public ItemEstoque exibirItemEstoque(@PathVariable String codBarras){
        return estoqueService.buscarPorCod(codBarras);
    }

}
