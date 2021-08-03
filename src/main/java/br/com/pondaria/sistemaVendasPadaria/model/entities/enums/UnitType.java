package br.com.pondaria.sistemaVendasPadaria.model.entities.enums;

public enum UnitType {
    UNIT("unit"),
    WEIGHT("weight");

    private String selectedUnit;

    private UnitType(String unit) {
        this.selectedUnit = unit;
    }
}
