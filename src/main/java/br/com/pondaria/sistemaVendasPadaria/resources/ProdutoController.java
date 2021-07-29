package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    //Feito
    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO cadastrarProduto(@RequestBody Produto produto){
        return produtoService.criarNovoProduto(produto);
    }

    //Feito
    @GetMapping("/listarCadastrados")
    public List<Produto> verificarProduto(){
        return produtoService.listarProdutos();
    }

    // verificar retorno quando não há o produto
    @GetMapping("/descricao/{descricao}")
    public Produto buscarPorDescricao(@PathVariable String descricao){
        try{
            return produtoService.buscarProdutoDescricao(descricao);
        }catch (IllegalArgumentException e){
            System.out.println(MessageDTO.builder().msg(e.getMessage()).build());
            return null;
        }
    }

    @GetMapping("/codigoBarras/{codBarras}")
    public Produto buscarPorCodBarras(@PathVariable String codBarras){
        try{
            return produtoService.buscarProdutoCodBarras(codBarras);
        }catch (IllegalArgumentException e){
            System.out.println(MessageDTO.builder().msg(e.getMessage()).build());
            return null;
        }
    }

    @GetMapping("/ativos")
    public List<Produto> listarProdutosAtivos(){
        return produtoService.buscarProdutosAtivos();
    }

    @GetMapping("/inativos")
    public List<Produto> listarProdutosInativos(){
        return produtoService.buscarProdutosInativos();
    }

    //Revisar o SQL, utilizar o EntityManager para instanciar manualmente a atualização
    /*@PutMapping("/atualizarproduto")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageDTO atualizarProduto(@RequestBody Produto produto){
        return produtoService.atualizarProduto(produto);
    }*/


}
