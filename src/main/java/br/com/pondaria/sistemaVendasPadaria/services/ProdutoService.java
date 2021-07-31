package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.StatusProduto;
import br.com.pondaria.sistemaVendasPadaria.model.entities.produtos.Produto;
import br.com.pondaria.sistemaVendasPadaria.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private ProdutoRepository repository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository){
        repository = produtoRepository;
    }

    public MessageDTO criarNovoProduto (Produto produto){
        if(verificarExistencia(produto)) return MessageDTO.builder().msg("Produto já existente").build();
        else if(produto.getStatus().equals(StatusProduto.INATIVO)) return MessageDTO.builder().msg("Status inválido!").build();
        else{
            repository.save(produto);
            return MessageDTO.builder().msg("Produto Criado com Sucesso!").build();
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

    public Optional<Produto> buscarProdutoDescricao(String descricao){
        Optional<Produto> produtoBuscado = repository.buscarPelaDescricao(descricao);
        return produtoBuscado;
    }

    public Optional<Produto> buscarProdutoCodBarras(String codBarras){
        Optional<Produto> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        return produtoBuscado;
    }

    public MessageDTO atualizarProdutoDescricao(String codBarras, String novaDescricao) {
        Optional<Produto> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        if(!produtoBuscado.isPresent()) return MessageDTO.builder().msg("Produto não cadastrado").build();
        else{
            repository.atualizarDescricaoProduto(novaDescricao,codBarras);
            return MessageDTO.builder().msg("Atualização realizada com sucesso! "
                    +repository.buscarPeloCodigoBarras(codBarras).get()).build();
        }
    }

    public MessageDTO atualizarProdutoInteiro(String codBarras, Produto novoProduto) {
        Optional<Produto> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        if(!produtoBuscado.isPresent()) return MessageDTO.builder().msg("Produto não cadastrado").build();
        else{
            repository.atualizarProdutoInteiro(novoProduto.getDescricao(),novoProduto.getValorCusto(),
                    novoProduto.getPesoUnitario(),novoProduto.getProdutoFabricado(),novoProduto.getUnidadeMedida(),
                    novoProduto.getValorDeVenda(),codBarras);
            return MessageDTO.builder().msg("Atualização realizada com sucesso!").build();
        }
    }

    //método para inativar um produto
    public MessageDTO inativarCadastroProduto(String codBarras){
        Optional<Produto> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        if(!produtoBuscado.isPresent()) return MessageDTO.builder().msg("Produto não cadastrado").build();
        else if(produtoBuscado.get().getStatus().equals("INATIVO")) return MessageDTO.builder().msg("Produto já está inativo").build();
        else{
            repository.inativarAtivarCadastroProduto(codBarras,"ATIVO");
            return MessageDTO.builder().msg("Cadastro do produto foi desativado").build();
        }
    }

    public MessageDTO ativarCadastroProduto(String codBarras){
        Optional<Produto> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        if(!produtoBuscado.isPresent()) return MessageDTO.builder().msg("Produto não cadastrado").build();
        else if(produtoBuscado.get().getStatus().equals("ATIVO")) return MessageDTO.builder().msg("Produto já está ativo").build();
        else{
            repository.inativarAtivarCadastroProduto(codBarras,"INATIVO");
            return MessageDTO.builder().msg("Cadastro do produto foi ativado").build();
        }
    }

}
