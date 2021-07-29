package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.StatusProduto;
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

    public MessageDTO criarNovoProduto (Produto produto){
        if(verificarExistencia(produto)){
            return MessageDTO.builder()
                    .msg("Produto já existente").build();
        }else{
            repository.save(produto);
            return MessageDTO.builder()
                    .msg("Produto Criado com Sucesso!").build();
        }
    }

    public List<Produto> listarProdutos(){
        return repository.findAll();
    }

    private Boolean verificarExistencia(Produto produto){
        Integer verificacaoNoBanco = repository.verificar(produto.getCodigoBarras());
        if(verificacaoNoBanco != null && verificacaoNoBanco > 0) return true;
        else return false;
    }

    public List<Produto> buscarProdutosAtivos(){
        return repository.buscarProdutosAtivos(String.valueOf(StatusProduto.ATIVO));
    }

    public List<Produto> buscarProdutosInativos(){
        return repository.buscarProdutosAtivos(String.valueOf(StatusProduto.INATIVO));
    }

    public Produto buscarProdutoDescricao(String descricao){
        if(!repository.buscarPelaDescricao(descricao).isPresent()){
            throw new IllegalArgumentException("Erro! Produto não encontrado!");
        }else{
            return  repository.buscarPelaDescricao(descricao).get();
        }
    }
    public Produto buscarProdutoCodBarras(String codBarras){
        if(!repository.buscarPeloCodigoBarras(codBarras).isPresent()){
            throw new IllegalArgumentException("Erro! Produto não encontrado!");
        }else{
            return  repository.buscarPeloCodigoBarras(codBarras).get();
        }
    }

    /*public MessageDTO atualizarProduto(Produto produto) {
        Long id = repository.buscarProdutoPeloCodigo(produto.getCodigoBarras());
        Optional<Produto> produtoDoBanco = repository.findById(id);
        if(!produtoDoBanco.isPresent()){
            return MessageDTO.builder().msg("Produto não cadastrado").build();
        }else{
            repository.atualizarPeloCodigoBarras(produto.getDescricao(),produto.getValorCusto(),produto.getPesoUnitario(),
                    produto.getProdutoFabricado(),produto.getUnidadeMedida(),produto.getValorDeVenda(),produto.getCodigoBarras());
            return MessageDTO.builder().msg("Produto atualizado com sucesso!").build();
        }
    }*/
}
