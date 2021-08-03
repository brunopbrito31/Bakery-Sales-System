package br.com.pondaria.sistemaVendasPadaria.model.entities.users;

import br.com.pondaria.sistemaVendasPadaria.model.entities.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name ="tb_employees")
public abstract class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(name = "employee_registry",nullable = false, unique = true)
    private String registry;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Position position;

    @Column(name = "operational_password",nullable = false, unique = true)
    private String operationalPassword;
}
