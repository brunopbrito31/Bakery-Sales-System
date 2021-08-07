package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    //implementar tolowercase nas buscas

    private ProductService productService;

    @Autowired
    public ProductController(ProductService service){
        this.productService = service;
    }

    // ok - Testar
    @GetMapping
    public ResponseEntity<List<Product>> returnAllProducts(){
        List<Product> products = productService.showAllCadastredProducts();
        if(products.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(products);
    }

    // ok - Testar
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product){
        return productService.newProduct(product);
    }

    // ok - Testar
    @GetMapping("/searchbydescription")
    public ResponseEntity<Product> searchByDescription(@RequestParam(name = "description") String description){
        Optional<Product> searchedProduct = productService.searchProductByDescription(description);
        if(!searchedProduct.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(searchedProduct.get());
    }

    // ok - Testar
    @GetMapping("/searchbybarcode")
    public ResponseEntity<Product> searchByBarcode(@RequestParam(name = "barcode") String barcode){
        Optional<Product> searchedProduct = productService.searchProductByBarcode(barcode);
        if(!searchedProduct.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(searchedProduct.get());
    }

    // ok - Testar
    @GetMapping("/show-allactive")
    public ResponseEntity<List<Product>> returnAllActive(){
        List<Product> allActiveProducts = productService.searchActiveProducts();
        if(allActiveProducts.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(allActiveProducts);
    }

    // ok - Testar
    @GetMapping("/show-allinactive")
    public ResponseEntity<List<Product>> returnAllInactive(){
        List<Product> allInactiveProducts = productService.searchInactiveProducts();
        if(allInactiveProducts.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(allInactiveProducts);
    }


    @PutMapping("/update/description")
    public Product updateDescription(@RequestParam(name = "barcode") String barcode, @RequestBody String newDescription){
        return productService.updateProductDescription(barcode,newDescription);
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
