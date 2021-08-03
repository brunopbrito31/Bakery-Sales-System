package br.com.pondaria.sistemaVendasPadaria.model.entities.sales;

import br.com.pondaria.sistemaVendasPadaria.exceptions.SaleException;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "fatherSale")
    private List<SaleItem> salesItems;

    @Column(nullable = false)
    private BigDecimal totalValue;

    @Column(nullable = false)
    private Date startMoment;

    @Column
    private Date completeMoment;

    @Column
    private OrderStatus orderStatus;

    public static Sale saleStart() {
        Sale startedSale = new Sale(null, new ArrayList<>(), BigDecimal.valueOf(0d), Date.from(Instant.now()),null,OrderStatus.STARTED); // verificar a possibilidade de trocar para LocalDateTime
        return startedSale;
    }

    public void finishSale(){
        if(orderStatus.equals(null) || (!orderStatus.equals(OrderStatus.STARTED) && orderStatus.equals(OrderStatus.INPROGRESS))){
            throw new SaleException("Finish sale is just possible in status of sale in progress or started!");
        }
        this.orderStatus = OrderStatus.FINISH;
        this.setCompleteMoment(Date.from(Instant.now()));
    }

    public void cancelSale(){
        if(orderStatus.equals(null) || (!orderStatus.equals(OrderStatus.STARTED) && !orderStatus.equals(OrderStatus.INPROGRESS))){
            throw new SaleException("Cancel sale  is just possible in status of sale in progress or started!");
        }
        this.orderStatus = OrderStatus.CANCELED;
        this.setCompleteMoment(Date.from(Instant.now()));
    }

    public void addItem(SaleItem saleItem){
        if(orderStatus.equals(null) || (!orderStatus.equals(OrderStatus.INPROGRESS) && !orderStatus.equals(OrderStatus.STARTED))){
            throw new SaleException("Not is possible add a item before sale start");
        }else {
            if(orderStatus.equals(OrderStatus.STARTED)) orderStatus = OrderStatus.INPROGRESS;
            this.salesItems.add(saleItem);
            this.setTotalValue(totalValue.add(saleItem.getTotalvalue()));
        }
    }

    public String viewCart(){
        StringBuilder sb = new StringBuilder();
        sb.append("Items in cart******************* ").append("\n");
        for(SaleItem x : salesItems){
            sb.append(x.itemSaleDetail()).append("\n");
            sb.append("------------------------------").append("\n");
        }
        return sb.toString();
    }
}
