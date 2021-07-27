package br.com.pondaria.sistemaVendasPadaria.model.entities.usuario;

import br.com.pondaria.sistemaVendasPadaria.model.entities.venda.Estoque;

import java.util.ArrayList;
import java.util.List;

public class Administrador extends Funcionario {

    //Atributos
    private String email;
    private List<Double> despesas;

    //Construtores
    public Administrador(String CPF, String matricula, String senhaOperacional, String nome, String telefone, Integer cargaHoraria, Double salario, String email) {
        super(CPF, matricula, senhaOperacional, nome, telefone, cargaHoraria, salario);
        this.email = email;
        this.despesas = new ArrayList<>();
    }

    public Administrador() {
        this.despesas = new ArrayList<>();

    }

    //Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //ToString
    @Override
    public String toString() {
        return super.toString() + "Administrador{" +
                "email='" + email + '\'' +
                '}';
    }

    //Métodos da classe
    public void Relatorio(Estoque estoque) {
        estoque.listarMovimentacoes();
    }

    public void pagarContas() {
        //Luz 500 //Agua 200 //Funcionarios 8000
        double total = 500 + 200 + 8000;
        for (Double despesa : despesas) {
            total += despesa;
        }
        //Verificar como pagar


    }

    public void adicionarDespesa(double despesa) {
        this.despesas.add(despesa);
    }


}
