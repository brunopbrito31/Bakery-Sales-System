package br.com.pondaria.sistemaVendasPadaria.model.entities.usuario;

import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.Produto;

public class Padeiro extends Funcionario {

    //Atributos
    private Double farinhaTrigo;
    private Double fermento;
    private Double sal;
    private Double acucar;
    private Double margarina;
    private Double ovos;
    private Double leite;
    private Double cafe;
    private Double pao;
    private Double presunto;
    private Double queijo;
    private Double abacaxi;
    private Double laranja;
    private Estoquista estoquista;

    //Construtor
    public Padeiro(String CPF, String matricula, String senhaOperacional, String nome, String telefone, Integer cargaHoraria, Double salario) {
        super(CPF, matricula, senhaOperacional, nome, telefone, cargaHoraria, salario);
    }

    public Padeiro() {

    }


    public void fabricarPão() {
        // 1.5kg Farinha trigo, 10g Fermento, 15g Sal, 20g Açucar, 10g Margarina. = 20 Pães

    }

    public void fabricarBolo() {
        // 15 ovos, 600g Açucar, 1,5kg Farinha de trigo, 200g Margarina, 5cx Leite. = 5 Bolo
    }

    public void fabricarCafe() {
        // 1kg Cafe, 2kg Açucar = 20 Cafés
    }

    public void fabricarMistoQuente() {
        // 5 Pao, 100g Queijo, 100g Presunto = 5 Mistos
    }

    public void fabricarSuco(String tipo) {
        // 2 kg fruta = 5 Copos
    }
}
