package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProdutoService {


    private ProdutoRepository repository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository){
        repository = produtoRepository;
    }

    /*public MessageDTO criarNovoProduto (Produto produto){
        if(verificarExistencia(produto)){
            return MessageDTO.builder()
                    .msg("Produto já existente").build();
        }else{
            repository.save(produto);
            return MessageDTO.builder()
                    .msg("Produto Criado com Sucesso!").build();
        }
    }*/

    public List<Produto> listarProdutos(){
        return repository.findAll();
    }

    /*public Boolean verificarExistencia(Produto produto){
        if(repository.verificar(produto.getCodigoBarras()).isPresent()) return true;
        else return false;
    }*/

}
