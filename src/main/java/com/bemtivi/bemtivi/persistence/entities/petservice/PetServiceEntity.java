package com.bemtivi.bemtivi.persistence.entities.petservice;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_servicos")
public class PetServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "servico_id")
    private String id;
    @NotNull
    @Column(name = "nome")
    private String name;
    @NotNull
    @Column(name = "preco")
    private BigDecimal price;
    @NotNull
    @Column(name = "duracao_estimada")
    private LocalTime estimated_duration;
    @NotNull
    @Column(name = "caminho_da_imagem")
    private String pathImage;
    @NotNull
    @Column(name = "descricao")
    private String description;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
