package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.ItemVenda;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.Venda;
import br.com.pondaria.sistemaVendasPadaria.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private VendaService vendaService;

    @Autowired
    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    // ok Testar
    @GetMapping("/iniciarVenda")
    public MessageDTO iniciarVenda(){
        return vendaService.iniciarVenda();
    }

    // ok Testar, poderia ser um DeletMapping?
    @GetMapping("/cancelarVenda")
    public MessageDTO cancelarVendaAtual(){
        return vendaService.cancelarVenda();
    }

    // ok - Testar
    @PostMapping("/carrinhoatual/adicionarProduto/{codBarras}")
    public MessageDTO adicionarProduto(@PathVariable String codBarras,@RequestBody BigDecimal quantidade){
        return vendaService.adicionarItemVenda(codBarras,quantidade);
    }

    // implementar remover produto

    // ok - Testar
    @GetMapping("/listarVendas")
    public ResponseEntity<List<Venda>> retornarVendas(){
        List<Venda> vendas = vendaService.retornarVendas();
        if(vendas.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(vendas);
    }

    // ok - Testar
    @GetMapping("/listarVendas/{id}")
    public ResponseEntity<Venda> listarVendaEspecifica(@PathVariable Long id){
        Optional<Venda> opVenda = vendaService.retornarVenda(id);
        if(!opVenda.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(opVenda.get());
    }


    @GetMapping("/carrinhoAtual")
    public ResponseEntity<List<ItemVenda>> mostrarCarrinho(){
        List<ItemVenda> itensVenda = vendaService.listarProdutos();
        if(itensVenda.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(itensVenda);
    }

}
