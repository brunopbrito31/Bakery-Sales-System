/*package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.model.entities.sales.SaleItem;
import br.com.pondaria.sistemaVendasPadaria.model.entities.sales.Sale;
import br.com.pondaria.sistemaVendasPadaria.repositories.StockItemRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.SaleItemRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProductRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalleService {

    private SaleRepository saleRepository;
    private SaleItemRepository saleItemRepository;
    private StockItemRepository stockItemRepository;
    private ProductRepository productRepository;
    private StockService stockService;
    private Sale venda;
    private Integer vendasIniciadas;

    @Autowired
    public SalleService
            (SaleRepository saleRepository, SaleItemRepository saleItemRepository,
             StockItemRepository stockItemRepository,
             ProductRepository productRepository, @SessionAttribute("estoqueService") StockService stockService, Sale venda) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.stockItemRepository = stockItemRepository;
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.venda = venda;
        this.vendasIniciadas = 0;
    }

    public MessageDTO adicionarItemVenda(String codBarras, BigDecimal quantidade) {
        if (this.vendasIniciadas.equals(0))
            return MessageDTO.builder().msg("N??o ?? poss??vel adicionar um produto sem inciar uma venda antes!").build();
        if (quantidade.compareTo(BigDecimal.valueOf(0)) < 0)
            return MessageDTO.builder().msg("Quantidade Inv??lida!").build();
        Optional<Product> produto = productRepository.buscarPeloCodigoBarras(codBarras);
        if (!produto.isPresent()) return MessageDTO.builder().msg("Produto inv??lido").build();
        Optional<SaleItem> itemVendaExistente = this.venda.getItensVenda().stream().filter(x -> x.getProduct().getCodigoBarras().equals(produto.get().getCodigoBarras())).findFirst();
        if (itemVendaExistente.isPresent()) {
            stockService.retirarItemNoEstoque(codBarras, quantidade, MovementType.VENDA);
            itemVendaExistente.get().setQuantidade(itemVendaExistente.get().getQuantidade().add(quantidade));
            BigDecimal valorTotalItemAtual = produto.get().getValorDeVenda().multiply(quantidade);
            itemVendaExistente.get().setVlrTotal(itemVendaExistente.get().getVlrTotal().add(valorTotalItemAtual));
            //itemVendaRepository.save(itemVendaExistente.get());
        } else {
            SaleItem saleItem = SaleItem.builder()
                    .vendaPai(this.venda)
                    .quantidade(quantidade)
                    .product(produto.get())
                    .vlrTotal(produto.get().getValorDeVenda().multiply(quantidade))
                    .build();
            try {
                stockService.retirarItemNoEstoque(saleItem.getProduct().getCodigoBarras(), saleItem.getQuantidade(), MovementType.VENDA);
                this.venda.getItensVenda().add(saleItem);
                //itemVendaRepository.save(itemVenda);
            } catch (IllegalArgumentException e) {
                return MessageDTO.builder().msg("Error: " + e.getMessage()).build();
            }
        }
        return MessageDTO.builder().msg("Adicionado ao carrinho! Descri????o: " + produto.get().getDescricao() + " Quantidade: " + quantidade).build();
    }

    public MessageDTO removerItens(String codBarras, BigDecimal quantidade) {
        if (this.vendasIniciadas.equals(0))
            return MessageDTO.builder().msg("N??o ?? poss??vel remover um produto sem inciar uma venda antes!").build();
        if (quantidade.compareTo(BigDecimal.valueOf(0)) < 0)
            return MessageDTO.builder().msg("Quantidade Inv??lida!").build();
        Optional<Product> produto = productRepository.buscarPeloCodigoBarras(codBarras);
        if (!produto.isPresent()) return MessageDTO.builder().msg("Produto inv??lido").build();
        Optional<SaleItem> itemVendaExistente = this.venda.getItensVenda().stream().filter(x -> x.getProduct().getCodigoBarras().equals(produto.get().getCodigoBarras())).findFirst();
        if (itemVendaExistente.isPresent()) {
            if (quantidade.compareTo(itemVendaExistente.get().getQuantidade()) > 0) return MessageDTO.builder()
                    .msg("N??o ?? poss??vel retirar mais itens do que possui no carrinho!").build();
            stockService.adicionarItemNoEstoque(codBarras, quantidade, MovementType.ESTORNO);
            itemVendaExistente.get().setQuantidade(itemVendaExistente.get().getQuantidade().add(quantidade.multiply(BigDecimal.valueOf(-1))));
            BigDecimal valorTotalItemAtual = (produto.get().getValorDeVenda().multiply(quantidade)).multiply(BigDecimal.valueOf(-1));
            itemVendaExistente.get().setVlrTotal(itemVendaExistente.get().getVlrTotal().add(valorTotalItemAtual));
            if (itemVendaExistente.get().getQuantidade().equals(BigDecimal.valueOf(0)))
                this.venda.getItensVenda().remove(itemVendaExistente);
        } else {
            return MessageDTO.builder().msg("Produto n??o existente no carrinho!").build();
        }
        return MessageDTO.builder().msg("Retirado do carrinho! Descri????o: " + produto.get().getDescricao() + " Quantidade: " + quantidade).build();
    }


    public List<Sale> retornarVendas() {
        return saleRepository.findAll();
    }

    public Optional<Sale> retornarVenda(Long id) {
        Optional<Sale> vendaProcurada = saleRepository.findById(id);
        return vendaProcurada;
    }

    public MessageDTO iniciarVenda() {
        if (vendasIniciadas != 0)
            return MessageDTO.builder().msg("S?? ?? poss??vel trabalhar com uma venda por vez!").build();
        try {
            this.venda = Sale.abrirVenda();
            saleRepository.save(this.venda);
            vendasIniciadas = 1;
            return MessageDTO.builder().msg("Venda iniciada!").build();
        } catch (IllegalArgumentException e) {
            return MessageDTO.builder().msg("Erro ao abrir a venda: " + e.getMessage()).build();
        }
    }

    public BigDecimal valorTotalComp() {
        BigDecimal total = BigDecimal.valueOf(0);
        for (SaleItem x : venda.getItensVenda()) {
            total = total.add(x.getVlrTotal());
        }
        return total;
    }

    public MessageDTO confirmarVenda() {
        if (vendasIniciadas.equals(0)) return MessageDTO.builder().msg("N??o h?? vendas abertas").build();
        saleItemRepository.saveAll(venda.getItensVenda());
        vendasIniciadas = 0;
        return MessageDTO.builder().msg("Venda Confirmada \n" +
                "Valor Total R$: " + valorTotalComp()).build();

    }

    public MessageDTO cancelarVenda() {
        if (vendasIniciadas == 0) return MessageDTO.builder().msg("N??o h?? nenhuma venda para cancelar!").build();
        for (SaleItem x : venda.getItensVenda()) {
            stockService.adicionarItemNoEstoque(x.getProduct().getCodigoBarras(), x.getQuantidade(), MovementType.ESTORNO);
        }
        saleRepository.delete(this.venda);
        vendasIniciadas = 0;
        return MessageDTO.builder().msg("Venda Cancelada").build();
    }

    public List<String> listarProdutos() {
        if (this.venda.equals(null)) throw new IllegalArgumentException("N??o h?? venda iniciada!");
        else return this.venda.getItensVenda().stream().map(x -> x.exibirParaVenda()).collect(Collectors.toList());
    }

}*/
