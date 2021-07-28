package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum TipoMovimentacao {

    ENTRADA("entrada"),
    BAIXAINTERNA("baixainterna"),
    ESTORNO("estorno"),
    VENDA("venda");

    private String movimentacaoSelecionada;

    private TipoMovimentacao(String movimentacao) {
        this.movimentacaoSelecionada = movimentacao;
    }
}
