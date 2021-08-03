package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum OrderStatus {
    STARTED("started"),
    INPROGRESS("inprogress"),
    FINISH("finish"),
    CANCELED("canceled");

    private String selectedStatus;

    private OrderStatus(String status) {
        this.selectedStatus = status;
    }
}
