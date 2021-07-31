package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposito.ItemEstoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import br.com.pondaria.sistemaVendasPadaria.services.EstoqueService;
import br.com.pondaria.sistemaVendasPadaria.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private EstoqueService estoqueService;
    private ProdutoService produtoService;
    private ProdutoRepository produtoRepository; // passsar para a camada de service depois

    @Autowired
    public EstoqueController(EstoqueService estoqueService, ProdutoService produtoService, ProdutoRepository produtoRepository) {
        this.estoqueService = estoqueService;
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
    }

    @PostMapping("/adicionar/{codBarras}")
    public MessageDTO adicionarItem(@PathVariable String codBarras, @RequestBody BigDecimal quantidade){
        try{
            estoqueService.adicionarItemNoEstoque(codBarras,quantidade, TipoMovimentacao.ENTRADA);
            return MessageDTO.builder().msg("Adicionado com sucesso!").build();
        }catch (IllegalArgumentException e){
            return MessageDTO.builder().msg("Error: "+e.getMessage()).build();
        }
    }

    @PostMapping("/remover/{codBarras}")
    public MessageDTO removerItem(@PathVariable String codBarras, @RequestBody BigDecimal quantidade){
        try{
            estoqueService.retirarItemNoEstoque(codBarras,quantidade, TipoMovimentacao.BAIXAINTERNA);
            return MessageDTO.builder().msg("Removido com sucesso!").build();
        }catch (IllegalArgumentException e){
            return MessageDTO.builder().msg("Error: "+e.getMessage()).build();
        }
    }

    @GetMapping("/itens")
    public ResponseEntity<List<ItemEstoque>> exibirItens(){
        List<ItemEstoque> tdsEstoque = estoqueService.mostrarTodosItens();
        if(tdsEstoque.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(estoqueService.mostrarTodosItens());
    }

    @GetMapping("/itens/{codBarras}")
    public ResponseEntity<ItemEstoque> mostrarItem(@PathVariable String codBarras){
        Optional<ItemEstoque> itemEstoque = estoqueService.verificarEstoqueProduto(codBarras);
        if(!itemEstoque.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(itemEstoque.get());
    }


    /*

    // Feito - Testar
    @GetMapping("/exibir/{codBarras}")
    public ResponseEntity<ItemEstoque> exibirItemEstoque(@PathVariable String codBarras) {
        Optional<ItemEstoque> itemEstoqueSelecionado = estoqueService.verificarEstoqueProduto(codBarras);
        if(!itemEstoqueSelecionado.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(itemEstoqueSelecionado.get());
    }

    //Feito -- Testar depois
    /*@GetMapping("/movimentacao/{d1}to{d2}")
    public List<Movimentacao> verificarMovPeriodo(@PathVariable Date d1, @PathVariable Date d2) {
        try {
            return estoqueService.verificarMovPeriodo(d1, d2);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }*/
}
