package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    //implementar tolowercase nas buscas

    private ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService service){
        this.produtoService = service;
    }

    //Feito - ok -> existe alguma validação para produtos fabricados?
    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO cadastrarProduto(@RequestBody Produto produto){
        return produtoService.criarNovoProduto(produto);
    }

    //Lista os produtos cadastrados
    @GetMapping("/exibirtodos")
    public List<Produto> verificarProduto(){
        return produtoService.listarProdutos();
    }

    //Busca um produto pela descrição
    @GetMapping("/buscarpordescricao/{descricao}")
    public ResponseEntity<Produto> buscarPorDescricao(@PathVariable String descricao){
        Optional<Produto> produtoBuscado = produtoService.buscarProdutoDescricao(descricao);
        if(produtoBuscado.isPresent()) return ResponseEntity.ok().body(produtoBuscado.get());
        else return ResponseEntity.notFound().build();
    }

    //Busca um produto pelo código de barras
    @GetMapping("/buscarporcodigobarras/{codBarras}")
    public ResponseEntity<Produto> buscarPorCodBarras(@PathVariable String codBarras){
        Optional<Produto> produtoBuscado = produtoService.buscarProdutoCodBarras(codBarras);
        if(produtoBuscado.isPresent()) return ResponseEntity.ok().body(produtoBuscado.get());
        else return ResponseEntity.notFound().build();
    }

    //Lista os produtos ativos
    @GetMapping("/listarativos")
    public List<Produto> listarProdutosAtivos(){
        return produtoService.buscarProdutosAtivos();
    }

    //Lista os produtos inativos
    @GetMapping("/listarinativos")
    public List<Produto> listarProdutosInativos(){
        return produtoService.buscarProdutosInativos();
    }

    //Atualiza a descrição do produto do código de barras digitado na URL
    @PutMapping("/atualizardescricao/{codBarras}")
    public MessageDTO atualizarDescricao(@PathVariable String codBarras, @RequestBody String novaDescricao){
        return produtoService.atualizarProdutoDescricao(codBarras,novaDescricao);
    }

    //Atualiza o produto do código de barras digitado pelo produto entregue como parâmetro
    @PutMapping("/atualizar/{codBarras}")
    public MessageDTO atualizarProduto(@PathVariable String codBarras, @RequestBody Produto novoProduto){
        return produtoService.atualizarProdutoInteiro(codBarras,novoProduto);
    }

    // QUEBROU O SQL
    @PutMapping("/inativar/{codBarras}")
    public MessageDTO desativarProduto(@PathVariable String codBarras){
        return produtoService.inativarCadastroProduto(codBarras);
    }

    //Preciso testar
    @PutMapping("/ativar/{codBarras}")
    public MessageDTO ativarProduto(@PathVariable String codBarras){
        return produtoService.ativarCadastroProduto(codBarras);
    }


}
