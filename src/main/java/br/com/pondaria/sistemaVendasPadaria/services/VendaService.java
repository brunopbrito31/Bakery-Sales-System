package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.TipoMovimentacao;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.ItemVenda;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.Venda;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemVendaRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    private VendaRepository vendaRepository;
    private ItemVendaRepository itemVendaRepository;
    private ItemEstoqueRepository itemEstoqueRepository;
    private ProdutoRepository produtoRepository;
    private EstoqueService estoqueService;
    private Venda venda;
    private Integer vendasIniciadas;

    @Autowired
    public VendaService
            (VendaRepository vendaRepository, ItemVendaRepository itemVendaRepository,
            ItemEstoqueRepository itemEstoqueRepository,
            ProdutoRepository produtoRepository, EstoqueService estoqueService, Venda venda)
    {
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueService = estoqueService;
        this.venda = venda;
        this.vendasIniciadas = 0;
    }

    public MessageDTO adicionarItemVenda(String codBarras, BigDecimal quantidade) {
        if (venda.equals(null))
            return MessageDTO.builder().msg("Não é possível adicionar um produto sem inciar uma venda antes!").build();
        Optional<Produto> produto = produtoRepository.buscarPeloCodigoBarras(codBarras);
        if (!produto.isPresent()) return MessageDTO.builder().msg("Produto inválido").build();
        Optional<ItemVenda> itemVendaExistente = venda.getItensVenda().stream().filter(x -> x.getProduto().equals(produto)).findFirst();
        if(itemVendaExistente.isPresent()){
            estoqueService.retirarItemNoEstoque(codBarras,quantidade,TipoMovimentacao.VENDA);
            itemVendaExistente.get().setQuantidade(itemVendaExistente.get().getQuantidade().add(quantidade));
            BigDecimal valorTotalItemAtual = produto.get().getValorDeVenda().multiply(quantidade);
            itemVendaExistente.get().setVlrTotal(itemVendaExistente.get().getVlrTotal().add(valorTotalItemAtual));
        }else{
            ItemVenda itemVenda = ItemVenda.builder()
                    .vendaPai(this.venda)
                    .quantidade(quantidade)
                    .produto(produto.get())
                    .vlrTotal(produto.get().getValorDeVenda().multiply(quantidade))
                    .build();
            try{
                estoqueService.retirarItemNoEstoque(itemVenda.getProduto().getCodigoBarras(),itemVenda.getQuantidade(), TipoMovimentacao.VENDA);
                venda.getItensVenda().add(itemVenda);
            }catch (IllegalArgumentException e){
                return MessageDTO.builder().msg("Error: "+e.getMessage()).build();
            }
        }
        return MessageDTO.builder().msg("Adicionado ao carrinho!").build();
    }

    /*public MessageDTO removerItens(String codBarras, BigDecimal quantidade) {

    }*/

    public List<Venda> retornarVendas() {
        return vendaRepository.findAll();
    }

    //ok
    public Optional<Venda> retornarVenda(Long id) {
        Optional<Venda> vendaProcurada = vendaRepository.findById(id);
        return vendaProcurada;
    }

    // ok
    public MessageDTO iniciarVenda() {
        if(vendasIniciadas != 0) return MessageDTO.builder().msg("Só é possível trabalhar com uma venda por vez!").build();
        try {
            this.venda = Venda.abrirVenda();
            vendaRepository.save(venda);
            vendasIniciadas = 1;
            return MessageDTO.builder().msg("Venda iniciada!").build();
        } catch (IllegalArgumentException e){
            return MessageDTO.builder().msg("Erro ao abrir a venda: "+e.getMessage()).build();
        }
    }

    // ok
    public MessageDTO confirmarVenda() {
        if(vendasIniciadas.equals(0)) return MessageDTO.builder().msg("Não há vendas abertas").build();
        itemVendaRepository.saveAll(venda.getItensVenda());
        return MessageDTO.builder().msg("Venda Confirmada \n" +
                "Valor Total R$: "+ venda.getValorTotal()).build();
    }

    // ok
    public MessageDTO cancelarVenda(){
        if(vendasIniciadas == 0) return MessageDTO.builder().msg("Não há nenhuma venda para cancelar!").build();
        for(ItemVenda x: venda.getItensVenda()) {
            estoqueService.adicionarItemNoEstoque(x.getProduto().getCodigoBarras(),x.getQuantidade(),TipoMovimentacao.ESTORNO);
        }
        vendaRepository.delete(this.venda);
        vendasIniciadas = 0;
        return MessageDTO.builder().msg("Venda Cancelada").build();
    }

    // ok
    public List<ItemVenda> listarProdutos(){
        if(this.venda.equals(null)) throw new IllegalArgumentException("Não há venda iniciada!");
        else return this.venda.getItensVenda();
    }

}
