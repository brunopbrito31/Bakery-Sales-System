package br.com.pondaria.sistemaVendasPadaria.model.entities.usuario;
import java.util.Objects;

public abstract class Usuario {

    //Atributos
    private long id;
    private String CPF;

    //Construtores
    public Usuario(String CPF) {
        this.CPF = CPF;
    }

    public Usuario() {

    }

    //Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    //Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //ToString
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", CPF='" + CPF + '\'' +
                '}';
    }
}
