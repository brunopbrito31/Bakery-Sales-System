package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum ProductStatus {

    ACTIVE("active"),
    INACTIVE("inactive");

    private String selectedStatus;

    private ProductStatus(String status) {
        this.selectedStatus = status;
    }
}
