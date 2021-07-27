package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.vendas.Venda;
import br.com.pondaria.sistemaVendasPadaria.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class VendaService {

    private VendaRepository vendaRepository;

    @Autowired
    public VendaService( VendaRepository vendaRepository){
        this.vendaRepository = vendaRepository;
    }

    public List<Venda> retornarVendas(){
        return vendaRepository.findAll();
    }

    public Venda retornarVenda(Long id){
        Optional<Venda> vendaProcurada = vendaRepository.findById(id);
        if(!vendaProcurada.isPresent()){
            throw new IllegalArgumentException("Venda não existente");
        }
        return vendaProcurada.get();
    }

    // implementar na classe de venda o método que processa a criação
    /*public MessageDTO criarVenda(){
        //
    }*/
}
