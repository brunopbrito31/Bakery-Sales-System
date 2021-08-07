/*package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.sales.Sale;
import br.com.pondaria.sistemaVendasPadaria.model.entities.sales.SaleItem;
import br.com.pondaria.sistemaVendasPadaria.services.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/sales")
public class SaleController {

    private SalleService salleService;

    @Autowired
    public SaleController(@SessionAttribute("vendaService") SalleService salleService) {
        this.salleService = salleService;
    }

    @GetMapping
    public ResponseEntity<List<Sale>> allSales() {
        //Método para exibir todas as vendas existentes
        return null;
    }

    // ok Testar
    @PostMapping("/start")
    public Sale startSale() {
        // Implementar método
        return null;
    }

    // ok Testar, poderia ser um DeletMapping?
    @GetMapping("/{idSale}/cancel")
    public MessageDTO cancelSale(@PathVariable Long idSale) {
        //verificar retorno e método http adequado
        return null;
    }

    @GetMapping("/{idSale}/end")
    public MessageDTO finishSale(@PathVariable Long idSale) {
        //verificar retorno e método http adequado
        return null;
    }

    @PostMapping("/{idsale}/addProduct/{prodCode}")
    public SaleItem adicionarProduto(@PathVariable String idSale, @PathVariable String prodCode, @RequestBody BigDecimal quantity) {
        //Método que cria um novo item de venda e adiciona a venda existente
        return null;
    }

    @PutMapping("/{idSale}/removeProduct/{prodCode}")
    public SaleItem removeProduct(@PathVariable String idSale, @PathVariable String prodCode, @RequestBody BigDecimal quantity) {
        //Método que remove ou atualiza um item de venda
        return null;
    }



    @GetMapping("/{idSale}")
    public ResponseEntity<Sale> returnSaleById (@PathVariable Long idSale) {
        //Método que retorna uma venda especifica pelo Id
        return null;
    }

    @GetMapping("/{idSale}/cart")
    public ResponseEntity<List<String>> mostrarCarrinho() {
        //Método que mostra o carrinho de compras
        return null;
    }
}
*/