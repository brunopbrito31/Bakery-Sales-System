package br.com.pondaria.sistemaVendasPadaria.resources;


import br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response.MessageDTO;
import br.com.pondaria.sistemaVendasPadaria.model.entities.fabricacao.Insumo;
import br.com.pondaria.sistemaVendasPadaria.services.FabricacaoService;
import br.com.pondaria.sistemaVendasPadaria.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fabricacao")
public class FabricacaoController {

    private FabricacaoService fabricacaoService;
    private ProdutoService produtoService;

    @Autowired
    public FabricacaoController(FabricacaoService fabricacaoService, ProdutoService produtoService) {
        this.fabricacaoService = fabricacaoService;
        this.produtoService = produtoService;
    }

    @PostMapping("/definicao/{codBarras}/{qtInsumos}")
    public MessageDTO definirIsumos(@PathVariable String codBarras, @PathVariable Integer qtInsumos, @RequestBody String[] insumos){
        try{
            for(String x: insumos){
                fabricacaoService.associarUmInsumo(codBarras,x);
            }
            return MessageDTO.builder().msg("Associação Concluida!").build();
        }catch (IllegalArgumentException e){
            MessageDTO.builder().msg("Erro: "+e.getMessage());
        }
        return MessageDTO.builder().msg("Definição Conluida").build();
    }

    @GetMapping("/fabricar/{codBarras}")
    public MessageDTO fabricarProduto(@PathVariable String codBarras){
        fabricacaoService.fabricarProduto(codBarras);
        return MessageDTO.builder().msg("Fabricação Concluída!").build();
    }

    @PostMapping("/fabricar/{codBarras}")
    public MessageDTO fabricarProdutos(@PathVariable String codBarras, @RequestBody Integer quantidade){
        for(int i = 0; i < quantidade; i++){
            try{
                fabricacaoService.fabricarProduto(codBarras);
            }catch (IllegalArgumentException e){
                return MessageDTO.builder().msg("Houve um erro durante a fabricação, somente "+i+" itens foram fabricados, descrição do erro: "+e.getMessage()).build();
            }
        }
        return MessageDTO.builder().msg("Fabricação concluida!").build();
    }
}
