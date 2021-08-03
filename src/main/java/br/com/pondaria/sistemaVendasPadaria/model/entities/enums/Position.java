package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum Position {

    MANAGER("manager"),
    STOCKIST("stockist"),
    SELLER("seller"),
    BAKER("baker");

    private String selectedPosition;

    private Position(String position) {
        this.selectedPosition = position;
    }
}
