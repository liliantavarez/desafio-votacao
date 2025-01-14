package com.db.api.models;

import com.db.api.enums.StatusCPF;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "associados")
public class Associado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 150)
    private String nome;
    @Column(length = 11)
    private String cpf;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_cpf")
    @Builder.Default
    private StatusCPF statusCPF = StatusCPF.PODE_VOTAR;

    public Associado(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public void setStatusCPF(StatusCPF statusCPF) {
        this.statusCPF = statusCPF;
    }
}
