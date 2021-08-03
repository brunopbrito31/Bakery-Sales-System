package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    //implementar tolowercase nas buscas

    private ProductService productService;

    @Autowired
    public ProductController(ProductService service){
        this.productService = service;
    }

    @GetMapping
    public List<Product> returnAllProducts(){
        return null;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product){
        return null;
    }

    @GetMapping("/searchbydescription/{description}")
    public ResponseEntity<Product> searchByDescription(@PathVariable String description){
        return null;
    }

    @GetMapping("/searchbybarcode/{barcode}")
    public ResponseEntity<Product> searchByBarcode(@PathVariable String barcode){
        return null;
    }

    @GetMapping("/allactive")
    public List<Product> returnAllActive(){
        return null;
    }

    @GetMapping("/allinactive")
    public List<Product> returnAllInactive(){
        return null;
    }

    @PutMapping("/updatedescription/{barcode}")
    public MessageDTO updateDescription(@PathVariable String barcode, @RequestBody String newDescription){
        return null;
    }

    @PutMapping("/update/{barcode}")
    public MessageDTO updateProduct(@PathVariable String barcode, @RequestBody Product newProduct){
        return null;
    }

    @PutMapping("/disable/{barcode}")
    public MessageDTO disableProduct(@PathVariable String barcode){
        return null;
    }

    @PutMapping("/enable/{barcode}")
    public MessageDTO enableProduct(@PathVariable String codBarras){
        return null;
    }


}
