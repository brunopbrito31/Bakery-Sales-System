package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.ProductStatus;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    //Atualizado dia 07/08/2021
    public Product newProduct (Product product){
        Optional<Product> searchedProduct = productRepository.searchProductByBarcode(product.getBarcode());
        if(searchedProduct.isPresent()) throw new IllegalArgumentException("Product is already in database!");
        if(product.getStatus().equals(ProductStatus.INACTIVE)) throw new IllegalArgumentException("Not is possible registry of inactive product");
        Product savedProduct = productRepository.save(searchedProduct.get());
        return savedProduct;
    }

    //Atualizado 07/08/2021
    public List<Product> showAllCadastredProducts(){
        return productRepository.findAll();
    }

    //Atualizado 07/08/2021
    public List<Product> searchActiveProducts(){
        return productRepository.searchProductsByStatus(String.valueOf(ProductStatus.ACTIVE));
    }

    //Atualizado 07/08/2021
    public List<Product> searchInactiveProducts(){
        return productRepository.searchProductsByStatus(String.valueOf(ProductStatus.INACTIVE));
    }

    //Atualizado 07/08/2021 - Implementar trim e tolowercase para não quebrar o código com usuários sem formataçaõ
    public Optional<Product> searchProductByDescription(String description){
        Optional<Product> searchedProduct = productRepository.searchByDescription(description);
        return searchedProduct;
    }

    //Atualizado 07/08/2021
    public Optional<Product> searchProductByBarcode(String barcode){
        Optional<Product> searchedProduct = productRepository.searchProductByBarcode(barcode);
        return searchedProduct;
    }

    //Atualizado 07/08/2021
    public Product updateProductDescription(String barcode, String newDescription) {
        Optional<Product> searchProduct = productRepository.searchProductByBarcode(barcode);
        if(!searchProduct.isPresent()) throw new IllegalArgumentException("Product not cadastred");
        else{
            Product updatedProduct = productRepository.udpdateDescriptionOfProduct(newDescription,barcode);
            return updatedProduct;
        }
    }

    //Atualizado 07/08/2021
    public Product updateProduct(Product product) {
        Optional<Product> searchedProduct = productRepository.findById(product.getId());
        if(!searchedProduct.isPresent()) searchedProduct = productRepository.searchProductByBarcode(product.getBarcode());
        if(!searchedProduct.isPresent()) throw new IllegalArgumentException("Product not exists in cadastry");
        else{
            Product updatedProduct = productRepository.updateProduct(
                    product.getDescription(),
                    product.getCostValue(),
                    product.getUnitWeight(),
                    product.getUnitMeasure(),
                    product.getSaleValue(),
                    product.getBarcode()
            );
            return updatedProduct;
        }
    }

    //Atualizado 07/08/2021
    public Product disabeProductcadastry(String barcode){
        Optional<Product> searchedProduct = productRepository.searchProductByBarcode(barcode);
        if(!searchedProduct.isPresent()) throw new IllegalArgumentException("Product not cadastred");
        else if(searchedProduct.get().getStatus().equals(String.valueOf(ProductStatus.INACTIVE))) throw new IllegalArgumentException("Product is already inactive");
        else{
            Product disabledProduct = productRepository.updateProductStatus(String.valueOf(ProductStatus.INACTIVE),barcode);
            return disabledProduct;
        }
    }

    //Atualizado 07/08/2021
    public Product activeProductCadastry(String barcode){
        Optional<Product> searchedProduct = productRepository.searchProductByBarcode(barcode);
        if(!searchedProduct.isPresent()) throw new IllegalArgumentException("Product not cadastred");
        else if(searchedProduct.get().getStatus().equals(String.valueOf(ProductStatus.ACTIVE))) throw new IllegalArgumentException("Product is already ative");
        else{
            Product activeProduct = productRepository.updateProductStatus(String.valueOf(ProductStatus.ACTIVE),barcode);
            return activeProduct;
        }
    }
}
