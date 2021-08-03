package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.MovementType;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
import br.com.pondaria.sistemaVendasPadaria.model.entities.sales.ItemSale;
import br.com.pondaria.sistemaVendasPadaria.model.entities.sales.Sale;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemVendaRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private VendaRepository vendaRepository;
    private ItemVendaRepository itemVendaRepository;
    private ItemEstoqueRepository itemEstoqueRepository;
    private ProdutoRepository produtoRepository;
    private EstoqueService estoqueService;
    private Sale venda;
    private Integer vendasIniciadas;

    @Autowired
    public VendaService
            (VendaRepository vendaRepository, ItemVendaRepository itemVendaRepository,
             ItemEstoqueRepository itemEstoqueRepository,
             ProdutoRepository produtoRepository, @SessionAttribute("estoqueService") EstoqueService estoqueService, Sale venda) {
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueService = estoqueService;
        this.venda = venda;
        this.vendasIniciadas = 0;
    }

    public MessageDTO adicionarItemVenda(String codBarras, BigDecimal quantidade) {
        if (this.vendasIniciadas.equals(0))
            return MessageDTO.builder().msg("Não é possível adicionar um produto sem inciar uma venda antes!").build();
        if (quantidade.compareTo(BigDecimal.valueOf(0)) < 0)
            return MessageDTO.builder().msg("Quantidade Inválida!").build();
        Optional<Product> produto = produtoRepository.buscarPeloCodigoBarras(codBarras);
        if (!produto.isPresent()) return MessageDTO.builder().msg("Produto inválido").build();
        Optional<ItemSale> itemVendaExistente = this.venda.getItensVenda().stream().filter(x -> x.getProduct().getCodigoBarras().equals(produto.get().getCodigoBarras())).findFirst();
        if (itemVendaExistente.isPresent()) {
            estoqueService.retirarItemNoEstoque(codBarras, quantidade, MovementType.VENDA);
            itemVendaExistente.get().setQuantidade(itemVendaExistente.get().getQuantidade().add(quantidade));
            BigDecimal valorTotalItemAtual = produto.get().getValorDeVenda().multiply(quantidade);
            itemVendaExistente.get().setVlrTotal(itemVendaExistente.get().getVlrTotal().add(valorTotalItemAtual));
            //itemVendaRepository.save(itemVendaExistente.get());
        } else {
            ItemSale itemSale = ItemSale.builder()
                    .vendaPai(this.venda)
                    .quantidade(quantidade)
                    .product(produto.get())
                    .vlrTotal(produto.get().getValorDeVenda().multiply(quantidade))
                    .build();
            try {
                estoqueService.retirarItemNoEstoque(itemSale.getProduct().getCodigoBarras(), itemSale.getQuantidade(), MovementType.VENDA);
                this.venda.getItensVenda().add(itemSale);
                //itemVendaRepository.save(itemVenda);
            } catch (IllegalArgumentException e) {
                return MessageDTO.builder().msg("Error: " + e.getMessage()).build();
            }
        }
        return MessageDTO.builder().msg("Adicionado ao carrinho! Descrição: " + produto.get().getDescricao() + " Quantidade: " + quantidade).build();
    }

    public MessageDTO removerItens(String codBarras, BigDecimal quantidade) {
        if (this.vendasIniciadas.equals(0))
            return MessageDTO.builder().msg("Não é possível remover um produto sem inciar uma venda antes!").build();
        if (quantidade.compareTo(BigDecimal.valueOf(0)) < 0)
            return MessageDTO.builder().msg("Quantidade Inválida!").build();
        Optional<Product> produto = produtoRepository.buscarPeloCodigoBarras(codBarras);
        if (!produto.isPresent()) return MessageDTO.builder().msg("Produto inválido").build();
        Optional<ItemSale> itemVendaExistente = this.venda.getItensVenda().stream().filter(x -> x.getProduct().getCodigoBarras().equals(produto.get().getCodigoBarras())).findFirst();
        if (itemVendaExistente.isPresent()) {
            if (quantidade.compareTo(itemVendaExistente.get().getQuantidade()) > 0) return MessageDTO.builder()
                    .msg("Não é possível retirar mais itens do que possui no carrinho!").build();
            estoqueService.adicionarItemNoEstoque(codBarras, quantidade, MovementType.ESTORNO);
            itemVendaExistente.get().setQuantidade(itemVendaExistente.get().getQuantidade().add(quantidade.multiply(BigDecimal.valueOf(-1))));
            BigDecimal valorTotalItemAtual = (produto.get().getValorDeVenda().multiply(quantidade)).multiply(BigDecimal.valueOf(-1));
            itemVendaExistente.get().setVlrTotal(itemVendaExistente.get().getVlrTotal().add(valorTotalItemAtual));
            if (itemVendaExistente.get().getQuantidade().equals(BigDecimal.valueOf(0)))
                this.venda.getItensVenda().remove(itemVendaExistente);
        } else {
            return MessageDTO.builder().msg("Produto não existente no carrinho!").build();
        }
        return MessageDTO.builder().msg("Retirado do carrinho! Descrição: " + produto.get().getDescricao() + " Quantidade: " + quantidade).build();
    }


    public List<Sale> retornarVendas() {
        return vendaRepository.findAll();
    }

    public Optional<Sale> retornarVenda(Long id) {
        Optional<Sale> vendaProcurada = vendaRepository.findById(id);
        return vendaProcurada;
    }

    public MessageDTO iniciarVenda() {
        if (vendasIniciadas != 0)
            return MessageDTO.builder().msg("Só é possível trabalhar com uma venda por vez!").build();
        try {
            this.venda = Sale.abrirVenda();
            vendaRepository.save(this.venda);
            vendasIniciadas = 1;
            return MessageDTO.builder().msg("Venda iniciada!").build();
        } catch (IllegalArgumentException e) {
            return MessageDTO.builder().msg("Erro ao abrir a venda: " + e.getMessage()).build();
        }
    }

    public BigDecimal valorTotalComp() {
        BigDecimal total = BigDecimal.valueOf(0);
        for (ItemSale x : venda.getItensVenda()) {
            total = total.add(x.getVlrTotal());
        }
        return total;
    }

    public MessageDTO confirmarVenda() {
        if (vendasIniciadas.equals(0)) return MessageDTO.builder().msg("Não há vendas abertas").build();
        itemVendaRepository.saveAll(venda.getItensVenda());
        vendasIniciadas = 0;
        return MessageDTO.builder().msg("Venda Confirmada \n" +
                "Valor Total R$: " + valorTotalComp()).build();

    }

    public MessageDTO cancelarVenda() {
        if (vendasIniciadas == 0) return MessageDTO.builder().msg("Não há nenhuma venda para cancelar!").build();
        for (ItemSale x : venda.getItensVenda()) {
            estoqueService.adicionarItemNoEstoque(x.getProduct().getCodigoBarras(), x.getQuantidade(), MovementType.ESTORNO);
        }
        vendaRepository.delete(this.venda);
        vendasIniciadas = 0;
        return MessageDTO.builder().msg("Venda Cancelada").build();
    }

    public List<String> listarProdutos() {
        if (this.venda.equals(null)) throw new IllegalArgumentException("Não há venda iniciada!");
        else return this.venda.getItensVenda().stream().map(x -> x.exibirParaVenda()).collect(Collectors.toList());
    }

}
