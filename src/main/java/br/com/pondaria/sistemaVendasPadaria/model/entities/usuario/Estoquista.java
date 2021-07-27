package br.com.pondaria.sistemaVendasPadaria.model.entities.usuario;

import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.Estoque;
import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.Lote;
import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.Produto;
import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.enums.TipoUnidade;

import java.time.LocalDateTime;

public class Estoquista extends Funcionario {

    //Atributos

    //Atributos relação
    private Estoque estoque;

    //Construtores
    public Estoquista(String CPF, String matricula, String senhaOperacional, String nome, String telefone, Integer cargaHoraria, Double salario, Estoque estoque) {
        super(CPF, matricula, senhaOperacional, nome, telefone, cargaHoraria, salario);
        this.estoque = estoque;
    }

    public Estoquista() {

    }

    //ToString


    @Override
    public String toString() {
        return super.toString() + "Estoquista{" +
                "estoque=" + estoque +
                '}';
    }

    //Métodos da classe
    public void verificaEstoque() {
        estoque.listarLotes();
    }

    public void adicionaProduto(Produto produto, double quantidade, TipoUnidade tipoUnidade) {
        Lote novoLote = new Lote(quantidade, tipoUnidade, LocalDateTime.now(), produto);
        estoque.entrada(novoLote);
    }

    public Double retornaProduto(Produto produto, double quantidade) {
        return this.estoque.transfereProduto(produto, quantidade);
    }

    public void retiraProdutosVencidos() {
        for (Lote lote : this.estoque.getLotes()) {
            if (lote.getProduto().getDataValidade().isBefore(LocalDateTime.now())) {
                System.out.println(lote.getProduto().getNome() + " passou do prazo de validade e está sendo jogado fora!");
                this.estoque.getLotes().remove(lote);
            }
        }
    }

    public Produto verificaCodBarras(String codbarras) {
        for (Lote lote : this.estoque.getLotes()) {
            if (codbarras.equalsIgnoreCase(lote.getProduto().getCodBArras())) {
                System.out.println("O produto " + lote.getProduto().getNome() + " foi encontrado!");
                return lote.getProduto();
            }
        }
        System.out.println("Nenhum produto foi encontrado com este código de barras");
        return null;
    }
}

