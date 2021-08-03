package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.StockItem;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.Movement;
import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import br.com.pondaria.sistemaVendasPadaria.services.EstoqueService;
import br.com.pondaria.sistemaVendasPadaria.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            estoqueService.adicionarItemNoEstoque(codBarras,quantidade, MovementType.ENTRADA);
            return MessageDTO.builder().msg("Adicionado com sucesso!").build();
        }catch (IllegalArgumentException e){
            return MessageDTO.builder().msg("Error: "+e.getMessage()).build();
        }
    }

    @PostMapping("/remover/{codBarras}")
    public MessageDTO removerItem(@PathVariable String codBarras, @RequestBody BigDecimal quantidade){
        try{
            estoqueService.retirarItemNoEstoque(codBarras,quantidade, MovementType.BAIXAINTERNA);
            return MessageDTO.builder().msg("Removido com sucesso!").build();
        }catch (IllegalArgumentException e){
            return MessageDTO.builder().msg("Error: "+e.getMessage()).build();
        }
    }

    @GetMapping("/itens")
    public ResponseEntity<List<StockItem>> exibirItens(){
        List<StockItem> tdsEstoque = estoqueService.mostrarTodosItens();
        if(tdsEstoque.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(estoqueService.mostrarTodosItens());
    }

    @GetMapping("/itens/{codBarras}")
    public ResponseEntity<StockItem> mostrarItem(@PathVariable String codBarras){
        Optional<StockItem> itemEstoque = estoqueService.verificarEstoqueProduto(codBarras);
        if(!itemEstoque.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(itemEstoque.get());
    }

    @GetMapping("/relatorio/movimentacoes/{inicio}/{fim}")
    public ResponseEntity<List<Movement>> filtrarMovimentacoes(@PathVariable String inicio, @PathVariable String fim){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date dataInicio = sdf.parse(inicio);
            Date dataFim = sdf.parse(fim);
            return ResponseEntity.ok().body(estoqueService.filtrarMovimentacoesData(dataInicio,dataFim));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
