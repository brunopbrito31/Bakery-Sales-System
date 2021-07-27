package br.com.pondaria.sistemaVendasPadaria.model.entities.venda;

import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.ItemVenda;
import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.Lote;
import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.enums.CategoriaProduto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Produto {

    //Atributos
    private Long id;
    private String nome;
    private CategoriaProduto categoriaProduto;
    private Double preco;
    private Double peso;
    private String codBArras;
    private LocalDateTime dataFabricacao;
    private LocalDateTime dataValidade;

    //Atributos relação
    List<ItemVenda> itemVenda;
    List<Lote> lotes;

    //Construtores
    public Produto(Long id, String nome, CategoriaProduto categoriaProduto, Double preco, Double peso,
                   String codBArras, LocalDateTime dataFabricacao, LocalDateTime dataValidade) {
        this.id = id;
        this.nome = nome;
        this.categoriaProduto = categoriaProduto;
        this.preco = preco;
        this.peso = peso;
        this.codBArras = codBArras;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
        this.itemVenda = new ArrayList<>();
        this.lotes = new ArrayList<>();
    }

    public Produto() {
        this.itemVenda = new ArrayList<>();
        this.lotes = new ArrayList<>();
    }

    //Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getCodBArras() {
        return codBArras;
    }

    public void setCodBArras(String codBArras) {
        this.codBArras = codBArras;
    }

    public LocalDateTime getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(LocalDateTime dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public LocalDateTime getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDateTime dataValidade) {
        this.dataValidade = dataValidade;
    }

    public List<ItemVenda> getItemVenda() {
        return itemVenda;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    //HashCode e Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //ToString
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoriaProduto=" + categoriaProduto +
                ", preco=" + preco +
                ", peso=" + peso +
                ", codBArras='" + codBArras + '\'' +
                ", dataFabricacao=" + dataFabricacao +
                ", dataValidade=" + dataValidade +
                '}';
    }

    //Métodos da classe
}
