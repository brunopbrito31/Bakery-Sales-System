package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum Cargo {
    ADMINISTRADOR("administrador"),
    ESTOQUISTA("estoquista"),
    VENDEDOR("vendedor"),
    PADEIRO("padeiro");

    private String cargoSelecionado;

    private Cargo(String cargo) {
        this.cargoSelecionado = cargo;
    }
}
