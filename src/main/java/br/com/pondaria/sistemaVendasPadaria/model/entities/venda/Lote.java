package br.com.pondaria.sistemaVendasPadaria.model.entities.venda;

import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.enums.TipoUnidade;

import java.time.LocalDateTime;
import java.util.Objects;

public class Lote {

    //Atributos
    private Long id;
    private Double quantidade;
    private TipoUnidade tipoUnidade;
    private LocalDateTime dataEntrada;

    //Atributos relação
    private Produto produto;

    //Construtores
    public Lote(Double quantidade, TipoUnidade tipoUnidade, LocalDateTime dataEntrada, Produto produto) {
        this.quantidade = quantidade;
        this.tipoUnidade = tipoUnidade;
        this.dataEntrada = dataEntrada;
        this.produto = produto;
    }

    public Lote() {

    }

    //Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    //HashCode e Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lote lote = (Lote) o;
        return id.equals(lote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //ToString
    @Override
    public String toString() {
        return "Lote{" +
                "id=" + id +
                ", quantidade=" + quantidade +
                ", dataEntrada=" + dataEntrada +
                ", produto=" + produto +
                '}';
    }

    //Métodos da classe
    public void exibirProduto() {
        System.out.println(this.produto.getNome());
    }
}
