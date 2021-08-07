package br.com.pondaria.sistemaVendasPadaria.resources;

import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping
    public ResponseEntity<List<Product>> returnAllProducts(){
        List<Product> products = productService.showAllCadastredProducts();
        if(products.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(products);
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product cadastredProduct = productService.newProduct(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cadastredProduct.getId()).toUri();
        return ResponseEntity.created(uri).body(cadastredProduct);
    }

    @GetMapping("/searchbydescription")
    public ResponseEntity<List<Product>> searchByDescription(@RequestParam(name = "description") String description){
        List<Product> searchedProduct = productService.searchProductByDescription(description);
        if(searchedProduct.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(searchedProduct);
    }

    @GetMapping("/searchbybarcode")
    public ResponseEntity<Product> searchByBarcode(@RequestParam(name = "barcode") String barcode){
        Optional<Product> searchedProduct = productService.searchProductByBarcode(barcode);
        if(!searchedProduct.isPresent()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(searchedProduct.get());
    }

    @GetMapping("/show-allactive")
    public ResponseEntity<List<Product>> returnAllActive(){
        List<Product> allActiveProducts = productService.searchActiveProducts();
        if(allActiveProducts.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(allActiveProducts);
    }

    @GetMapping("/show-allinactive")
    public ResponseEntity<List<Product>> returnAllInactive(){
        List<Product> allInactiveProducts = productService.searchInactiveProducts();
        if(allInactiveProducts.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(allInactiveProducts);
    }

    // funciona, só que o retorno ainda traz o objeto antigo na tela - Corrigir
    @PutMapping("/update/description/{barcode}")
    public ResponseEntity<Product> updateDescription(@PathVariable String barcode, @RequestBody String newDescription){
        Product updatedProduct = productService.updateProductDescription(barcode,newDescription);
        return ResponseEntity.ok().body(updatedProduct);
    }

    // funciona, mas precisa por o ID no corpo da requisição - deixar claro para o usuário
    @PutMapping("/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product newProduct){
        Product updatedProduct = productService.updateProduct(newProduct);
        return ResponseEntity.ok().body(updatedProduct);
    }

    // funciona, só que o retorno ainda traz o objeto antigo na tela - Corrigir
    @PutMapping("/disable/{barcode}")
    public ResponseEntity<Product> disableProduct(@PathVariable String barcode){
        Product disabledProduct = productService.disabeProductcadastry(barcode);
        return ResponseEntity.ok().body(disabledProduct);
    }

    @PutMapping("/enable/{barcode}")
    public ResponseEntity<Product> enableProduct(@PathVariable String barcode){
        Product enabledProduct = productService.activeProductCadastry(barcode);
        return ResponseEntity.ok().body(enabledProduct);
    }

}
