package br.com.pondaria.sistemaVendasPadaria.resources;


import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.Venda;
import br.com.pondaria.sistemaVendasPadaria.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
//@RequestMapping("/vendas")
public class VendaController {

    /*private VendaService vendaService;

    @Autowired
    public VendaController(VendaService vendaService){
        this.vendaService =vendaService;
    }

    @GetMapping
    public List<Venda> listarVendas(){
        return vendaService.retornarVendas();
    }

    @GetMapping("/{id}")
    public Venda retornarVenda(@PathVariable Long id){
        return vendaService.retornarVenda(id);
    }

    /*@PostMapping("/abrirVenda")
    public MessageDTO criarVenda(){

    }*/



    // criar uma venda*/

}
