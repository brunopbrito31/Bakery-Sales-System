package br.com.pondaria.sistemaVendasPadaria.model.entities.usuario;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Funcionario extends Usuario{

    //Atributos
    private String matricula;
    private String senhaOperacional;
    private String nome;
    private String telefone;
    private Integer cargaHoraria;
    private Double salario;
    private List<LocalDateTime> horariosDePonto;

    //Construtores
    public Funcionario(String CPF, String matricula, String senhaOperacional, String nome, String telefone, Integer cargaHoraria, Double salario) {
        super(CPF);
        this.matricula = matricula;
        this.senhaOperacional = senhaOperacional;
        this.nome = nome;
        this.telefone = telefone;
        this.cargaHoraria = cargaHoraria;
        this.salario = salario;
        this.horariosDePonto = new ArrayList<>();

    }

    public Funcionario() {

    }

    //Getters and Setters
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenhaOperacional() {
        return senhaOperacional;
    }

    public void setSenhaOperacional(String senhaOperacional) {
        this.senhaOperacional = senhaOperacional;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    //ToString
    @Override
    public String toString() {
        return "Funcionario{id=" + this.getId() +
                ", CPF=" + this.getCPF() +
                ", matricula='" + matricula + '\'' +
                ", senhaOperacional='" + senhaOperacional + '\'' +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                ", salario=" + salario +
                '}';
    }

    //Métodos da Classe
    public void marcarPonto() {
        this.horariosDePonto.add(LocalDateTime.now());
    }

    public void mostraHorariosPonto() {
        for (LocalDateTime dataHora : horariosDePonto) {
            System.out.println(dataHora);
        }
    }
}
