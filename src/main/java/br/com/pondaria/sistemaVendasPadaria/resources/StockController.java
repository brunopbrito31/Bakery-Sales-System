package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.Movement;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.StockItem;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProductRepository;
import br.com.pondaria.sistemaVendasPadaria.services.ProductService;
import br.com.pondaria.sistemaVendasPadaria.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    private StockService stockService;
    private ProductService productService;
    private ProductRepository productRepository;

    @Autowired
    public StockController(StockService stockService, ProductService productService, ProductRepository productRepository) {
        this.stockService = stockService;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping("/add/{barcode}")
    public ResponseEntity<StockItem> addStockItem(@PathVariable String barcode, @RequestBody BigDecimal quantity){
        StockItem stockItem = stockService.addStockItemInStock(barcode,quantity, MovementType.ENTRY);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(stockItem.getId()).toUri();
        return ResponseEntity.created(uri).body(stockItem);
    }

    @PutMapping("/remove/{barcode}") // método apenas para testar se a remoção está funcionando
    public ResponseEntity<StockItem> removeItem(@PathVariable String barcode, @RequestBody BigDecimal quantity){
       StockItem stockItem = stockService.removeItemInStock(barcode,quantity,MovementType.MANUFACTUREOUTPUT);
       return ResponseEntity.ok().body(stockItem);
    }

    @GetMapping("/items")
    public ResponseEntity<List<StockItem>> findAllItems(){
        List<StockItem> all = stockService.showAll();
        if(all.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(all);
    }

    @GetMapping("/items/{barcode}")
    public ResponseEntity<StockItem> mostrarItem(@PathVariable String barcode){
        //implementar método
        return null;
    }

    @GetMapping("/reports/movements/{startDate}/{endDate}")
    public ResponseEntity<List<Movement>> filtrarMovimentacoes(@PathVariable String startDate, @PathVariable String endDate){
        //implementar método
        return null;
    }

}
