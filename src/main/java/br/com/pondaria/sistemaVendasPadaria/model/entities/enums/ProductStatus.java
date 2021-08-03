package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum StatusProduto {

    ATIVO("ativo"),
    INATIVO("inativo");

    private String statusSelecionado;

    private StatusProduto(String status) {
        this.statusSelecionado = status;
    }
}
