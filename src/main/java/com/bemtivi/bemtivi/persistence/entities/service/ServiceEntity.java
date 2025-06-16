package com.bemtivi.bemtivi.persistence.entities.service;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.comment.CommentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_servicos")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "servico_id")
    private String id;
    @Column(name = "nome", nullable = false)
    private String name;
    @Column(name = "preco", nullable = false)
    private BigDecimal price;
    @Column(name = "duracao_estimada", nullable = false)
    private LocalTime estimatedDuration;
    @Column(name = "caminho_da_imagem", nullable = false)
    private String pathImage;
    @Column(name = "descricao", nullable = false)
    private String description;
    @OneToMany(mappedBy = "service", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<CommentEntity> comments;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
