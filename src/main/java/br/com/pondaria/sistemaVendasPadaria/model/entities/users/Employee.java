package br.com.pondaria.sistemaVendasPadaria.model.entities.users;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name ="tb_funcionario")
public abstract class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(nullable = false)
    private Double salario;

    @Column(nullable = false)
    // incluir mapeamento para string
    private Cargo cargo;

    @Column(nullable = false, unique = true)
    private String senhaOperacional;

    @Column(nullable = false)
    private Integer cargaHoraria;


    //private List<LocalDateTime> horariosDePonto;
}
