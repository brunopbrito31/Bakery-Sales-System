package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.Movement;
import br.com.pondaria.sistemaVendasPadaria.model.entities.deposit.StockItem;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.ProductStatus;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.repositories.MovementRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProductRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
public class StockService {

    private StockItemRepository stockItemRepository;

    private MovementRepository movementRepository;

    private ProductRepository productRepository;

    private List<StockItem> stockItems;

    private List<Movement> stockMovements;

    @Autowired
    public StockService(StockItemRepository stockItemRepository, MovementRepository movementRepository, ProductRepository productRepository, List<StockItem> stockItems, List<Movement> stockMovements) {
        this.stockItemRepository = stockItemRepository;
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
        this.stockItems = stockItems;
        this.stockMovements = stockMovements;
    }

    public StockItem addStockItemInStock(String barcode, BigDecimal quantity, MovementType movementType) {
        if(quantity.compareTo(BigDecimal.valueOf(0)) < 0) throw new IllegalArgumentException("Invalid quantity!");
        Optional<Product> searchedProduct = productRepository.searchProductByBarcode(barcode);
        if(!searchedProduct.isPresent()) throw new IllegalArgumentException("Product no exists");
        else if(searchedProduct.isPresent() && searchedProduct.get().getStatus().equals(ProductStatus.INACTIVE)){
            throw new IllegalArgumentException("Not is possible add product with inactive status");
        }else{
            Optional<StockItem> stockItemSearched = stockItemRepository.searchItemStockByBarcode(barcode);
            if(stockItemSearched.isPresent()){
                return updateOldStock(searchedProduct.get(),quantity, stockItemSearched.get(),movementType);
            }else{
                return addNewStock(searchedProduct.get(),quantity,movementType);
            }
        }
    }

    public StockItem removeItemInStock(String barcode, BigDecimal quantity, MovementType movementType){
        if(quantity.compareTo(BigDecimal.valueOf(0)) < 0) throw new IllegalArgumentException("Invalid quantity!");
        Optional<Product> searchedProduct = productRepository.searchProductByBarcode(barcode);
        if(!searchedProduct.isPresent()) throw new IllegalArgumentException("Product no exists");
        else if(searchedProduct.isPresent() && searchedProduct.get().getStatus().equals(ProductStatus.INACTIVE)){
            throw new IllegalArgumentException("Not is possible remove product with inactive status");
        }else{
            Optional<StockItem> stockItemSearched = stockItemRepository.searchItemStockByBarcode(barcode);
            if(stockItemSearched.isPresent()){
                if(stockItemSearched.get().getQuantity().compareTo(quantity) < 0) throw new IllegalArgumentException("Não há estoque suficiente :"+stockItemSearched.get().getQuantity());
                BigDecimal updateQuantity = quantity.multiply(BigDecimal.valueOf(-1));
                if(updateQuantity.equals(BigDecimal.valueOf(0))) {
                    stockItemRepository.deleteById(stockItemSearched.get().getId());
                    stockItemSearched.get().setQuantity(BigDecimal.valueOf(0));
                    stockItemSearched.get().setProductStatus(ProductStatus.INACTIVE);
                    return stockItemSearched.get();
                }
                else{
                    return updateOldStock(searchedProduct.get(),updateQuantity, stockItemSearched.get(),movementType);
                }
            }else{
                throw new IllegalArgumentException("Product no exists in stock");
            }
        }
    }

    // Atualizado 07/08/2021
    private StockItem addNewStock(Product productAdd, BigDecimal quantity,MovementType movementType){
        StockItem stockItem = StockItem.builder()
                .product(productAdd)
                .productStatus(ProductStatus.ACTIVE)
                .quantity(quantity).build();
        StockItem stockItemSaved = stockItemRepository.save(stockItem);
        movementRepository.save(Movement.builder()
                .quantity(quantity)
                .movementDate(Date.from(Instant.now()))
                .type(movementType)
                .movementedProduct(productAdd)
                .build());
        return stockItemSaved;
    }

    //Atualizado 07/08/2021
    private StockItem updateOldStock(Product productAdd, BigDecimal quantity, StockItem oldStockItem, MovementType movementType){
        StockItem oldStockItemCopy = oldStockItem;
        oldStockItemCopy.setQuantity(oldStockItemCopy.getQuantity().add(quantity));
        if(oldStockItemCopy.getQuantity().compareTo(BigDecimal.valueOf(0)) > 0) oldStockItemCopy.setProductStatus(ProductStatus.ACTIVE);
        else oldStockItemCopy.setProductStatus(ProductStatus.INACTIVE);
        stockItemRepository.updateQuantityAndStatus(oldStockItemCopy.getQuantity(),oldStockItemCopy.getProductStatus().toString(),oldStockItemCopy.getId());
        StockItem stockItemUpdated = stockItemRepository.searchItemStockByBarcode(productAdd.getBarcode()).get();
        movementRepository.save(Movement.builder()
                .quantity(quantity)
                .movementDate(Date.from(Instant.now()))
                .type(movementType)
                .movementedProduct(productAdd)
                .build());
        return stockItemUpdated;
    }

    //Atualizado 07/08/2021
    public List<StockItem> showAll() {
        return stockItemRepository.findAll();
    }


    public List<Movement> showMovementsByDate(Date inicio, Date fim){
        return null;
    }

    /*
    public List<Movement> filtraSaidasVendas(Date inicio, Date fim){
        return null;
    }

    private void sincronizarComBanco() {

    }*/
}
