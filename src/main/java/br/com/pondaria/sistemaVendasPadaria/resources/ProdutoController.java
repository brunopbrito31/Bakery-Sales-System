package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService service){
        this.produtoService = service;
    }

    /*@PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO cadastrarProduto(@RequestBody Produto produto){
        return produtoService.criarNovoProduto(produto);
    }*/

    @GetMapping("/listarCadastrados")
    public List<Produto> verificarProduto(){
        return produtoService.listarProdutos();
    }

    // Criar um metodo get

}
