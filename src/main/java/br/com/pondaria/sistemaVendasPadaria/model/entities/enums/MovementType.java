package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum MovementType {

    ENTRY("entry"),
    MANUFACTUREOUTPUT("manufactureOutput"),
    CHARGEBACK("chargeback"),
    SALEOUTPUT("venda");
    //Estudar a possibilidade de se ter uma devolução de produtos ao estoque por conta do cancelamento de fabricação

    private String movementSelected;

    private MovementType(String movement) {
        this.movementSelected = movement;
    }
}
