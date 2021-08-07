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

import java.math.BigDecimal;
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
    public StockItem addStockItem(@PathVariable String barcode, @RequestBody BigDecimal quantity){
        return stockService.addStockItemInStock(barcode,quantity, MovementType.ENTRY);
    }

    @DeleteMapping("/remove/{barcode}")
    public void removeItem(@PathVariable String barcode, @RequestBody BigDecimal quantity){
       //Implementar método e verificar declaração
    }

    @GetMapping("/items")
    public ResponseEntity<List<StockItem>> findAllItems(){
        //implementar método
        return null;
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
