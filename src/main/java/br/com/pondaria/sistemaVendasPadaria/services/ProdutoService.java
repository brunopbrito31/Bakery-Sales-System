package br.com.pondaria.sistemaVendasPadaria.services;

import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.ProductStatus;
import br.com.pondaria.sistemaVendasPadaria.model.entities.products.Product;
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

    public MessageDTO criarNovoProduto (Product product){
        if(verificarExistencia(product)) return MessageDTO.builder().msg("Produto já existente").build();
        else if(product.getStatus().equals(ProductStatus.INATIVO)) return MessageDTO.builder().msg("Status inválido!").build();
        else{
            repository.save(product);
            return MessageDTO.builder().msg("Produto Criado com Sucesso!").build();
        }
    }

    public List<Product> listarProdutos(){
        return repository.findAll();
    }

    private Boolean verificarExistencia(Product product){
        Integer verificacaoNoBanco = repository.verificar(product.getCodigoBarras());
        if(verificacaoNoBanco != null && verificacaoNoBanco > 0) return true;
        else return false;
    }

    public List<Product> buscarProdutosAtivos(){
        return repository.buscarProdutosAtivos(String.valueOf(ProductStatus.ATIVO));
    }

    public List<Product> buscarProdutosInativos(){
        return repository.buscarProdutosAtivos(String.valueOf(ProductStatus.INATIVO));
    }

    public Optional<Product> buscarProdutoDescricao(String descricao){
        Optional<Product> produtoBuscado = repository.buscarPelaDescricao(descricao);
        return produtoBuscado;
    }

    public Optional<Product> buscarProdutoCodBarras(String codBarras){
        Optional<Product> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        return produtoBuscado;
    }

    public MessageDTO atualizarProdutoDescricao(String codBarras, String novaDescricao) {
        Optional<Product> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        if(!produtoBuscado.isPresent()) return MessageDTO.builder().msg("Produto não cadastrado").build();
        else{
            repository.atualizarDescricaoProduto(novaDescricao,codBarras);
            return MessageDTO.builder().msg("Atualização realizada com sucesso! "
                    +repository.buscarPeloCodigoBarras(codBarras).get()).build();
        }
    }

    public MessageDTO atualizarProdutoInteiro(String codBarras, Product novoProduct) {
        Optional<Product> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        if(!produtoBuscado.isPresent()) return MessageDTO.builder().msg("Produto não cadastrado").build();
        else{
            repository.atualizarProdutoInteiro(novoProduct.getDescricao(), novoProduct.getValorCusto(),
                    novoProduct.getPesoUnitario(), novoProduct.getProductFabricado(), novoProduct.getUnidadeMedida(),
                    novoProduct.getValorDeVenda(),codBarras);
            return MessageDTO.builder().msg("Atualização realizada com sucesso!").build();
        }
    }

    //método para inativar um produto
    public MessageDTO inativarCadastroProduto(String codBarras){
        Optional<Product> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        if(!produtoBuscado.isPresent()) return MessageDTO.builder().msg("Produto não cadastrado").build();
        else if(produtoBuscado.get().getStatus().equals("INATIVO")) return MessageDTO.builder().msg("Produto já está inativo").build();
        else{
            repository.inativarAtivarCadastroProduto(codBarras,"ATIVO");
            return MessageDTO.builder().msg("Cadastro do produto foi desativado").build();
        }
    }

    public MessageDTO ativarCadastroProduto(String codBarras){
        Optional<Product> produtoBuscado = repository.buscarPeloCodigoBarras(codBarras);
        if(!produtoBuscado.isPresent()) return MessageDTO.builder().msg("Produto não cadastrado").build();
        else if(produtoBuscado.get().getStatus().equals("ATIVO")) return MessageDTO.builder().msg("Produto já está ativo").build();
        else{
            repository.inativarAtivarCadastroProduto(codBarras,"INATIVO");
            return MessageDTO.builder().msg("Cadastro do produto foi ativado").build();
        }
    }

    /*public MessageDTO criarNovoProduto (Produto produto){ // backup de cadastar produto
        if(verificarExistencia(produto)) return MessageDTO.builder().msg("Produto já existente").build();
        else if(produto.getStatus().equals(StatusProduto.INATIVO)) return MessageDTO.builder().msg("Status inválido!").build();
        else{
            repository.save(produto);
            return MessageDTO.builder().msg("Produto Criado com Sucesso!").build();
        }
    }*/

}
