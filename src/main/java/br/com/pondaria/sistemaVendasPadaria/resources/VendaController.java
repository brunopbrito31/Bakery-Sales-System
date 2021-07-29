package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.ItemVenda;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.Venda;
import br.com.pondaria.sistemaVendasPadaria.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private VendaService vendaService;

    @Autowired
    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping("/iniciarVenda")
    public MessageDTO iniciarVenda(){
        return vendaService.iniciarVenda();
    }

    @PostMapping("/vendaAtual/adicionarProduto")
    public MessageDTO adicionarProduto(ItemVenda itemVenda){
        return vendaService.adicionarItemVenda(itemVenda);
    }

    @GetMapping("/listarVendas")
    public List<Venda> retornarVendas(){
        return vendaService.retornarVendas();
    }

    @GetMapping("/listarVendas/{id}")
    public Venda listarVendaEspecifica(@PathVariable Long id){
        return vendaService.retornarVenda(id);
    }

    @GetMapping("/carrinhoAtual")
    public List<ItemVenda> mostrarCarrinho(){
        return vendaService.listarProdutos();
    }

    @GetMapping("/cancelarVenda")
    public MessageDTO cancelarVendaAtual(){
        return vendaService.cancelarVenda();
    }


}
