package br.com.pondaria.sistemaVendasPadaria.model.entities.usuario;

public class Cliente extends Usuario{
    //Setters desta classe são personalizados!

    //Atributos
    private Double saldoBonus;

    //Construtores
    public Cliente(String CPF) {
        super(CPF);
        this.saldoBonus = 0.0;
    }

    public Cliente() {
        this.saldoBonus = 0.0;
    }

    //Getters
    public Double getSaldoBonus() {
        return saldoBonus;
    }

    //ToString
    @Override
    public String toString() {
        return "Cliente{id=" + this.getId() +
                ", CPF=" + this.getCPF() +
                ", saldoBonus=" + saldoBonus +
                '}';
    }

    //Métodos da classe
    public void acrescentaBonus(double valor) {
        this.saldoBonus += valor;
    }

}
