package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.ItemVenda;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.Venda;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemEstoqueRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.ItemVendaRepository;
import br.com.pondaria.sistemaVendasPadaria.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    private VendaRepository vendaRepository;
    private ItemVendaRepository itemVendaRepository;
    private ItemEstoqueRepository itemEstoqueRepository;
    private EstoqueService estoqueService;
    private Venda venda;

    @Autowired
    public VendaService(VendaRepository vendaRepository, ItemVendaRepository itemVendaRepository, ItemEstoqueRepository itemEstoqueRepository, EstoqueService estoqueService, Venda venda) {
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.estoqueService = estoqueService;
        this.venda = venda;
    }

    public MessageDTO adicionarItemVenda(ItemVenda itemVenda) {
        if (venda.equals(null)) {
            return MessageDTO.builder().msg("Não é possível adicionar um produto sem inciar uma venda antes!").build();
        } else {
            estoqueService.retirarDoEstoqueVenda(itemVenda.getProduto(), itemVenda.getQuantidade());
            venda.getItensVenda().add(itemVenda);
            itemVendaRepository.save(itemVenda);
            return MessageDTO.builder().msg("Adicionado ao carrinho!").build();
        }
    }

    public List<Venda> retornarVendas() {
        return vendaRepository.findAll();
    }

    //ok
    public Venda retornarVenda(Long id) {
        Optional<Venda> vendaProcurada = vendaRepository.findById(id);
        if (!vendaProcurada.isPresent()) {
            throw new IllegalArgumentException("Venda não existente");
        }
        return vendaProcurada.get();
    }

    //Implementar que uma venda só pode ser inciada uma unica vez
    public MessageDTO iniciarVenda() {
        try {
            this.venda = Venda.abrirVenda();
            vendaRepository.save(venda);
            return MessageDTO.builder().msg("Venda iniciada!").build();
        } catch (IllegalArgumentException e){
            return MessageDTO.builder().msg("Erro ao abrir a venda: "+e.getMessage()).build();
        }
    }

    public MessageDTO cancelarVenda(){
        for(ItemVenda x: venda.getItensVenda()) {
            estoqueService.estornarAoEstoque(x.getProduto(),x.getQuantidade());
            itemVendaRepository.delete(x);
        }
        vendaRepository.delete(this.venda);
        // setar venda como nulo
        return MessageDTO.builder().msg("Venda Cancelada").build();
    }

    public List<ItemVenda> listarProdutos(){
        if(this.venda.equals(null)) throw new IllegalArgumentException("Não há venda iniciada!");
        else return this.venda.getItensVenda();
    }

    /*public MessageDTO removerProduto(){

    }*/
}
