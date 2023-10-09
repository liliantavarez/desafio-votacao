package com.db.api.models;

import com.db.api.enums.VotoEnum;
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
@Table(name = "votos")
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "sessao_id")
    private Sessao sessao;
    @ManyToOne
    @JoinColumn(name = "associado_id")
    private Associado associado;
    @Enumerated(EnumType.STRING)
    @Column(name = "voto")
    private VotoEnum votoEnum;

    public Voto(Sessao sessao, Associado associado, VotoEnum votoEnum) {
        this.sessao = sessao;
        this.associado = associado;
        this.votoEnum = votoEnum;
    }
}
