package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum ProductCategory {
    HOMEMADE("homemade"),
    INDUSTRIALIZED("industrialized");

    private String selectedCategory;

    private ProductCategory(String category) {
        this.selectedCategory = category;
    }
}
