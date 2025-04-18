package com.bemtivi.bemtivi.persistence.entities.category;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_categorias")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "categoria_id")
    private String id;
    @NotNull
    @Column(name = "nome")
    private String name;
    @NotNull
    @Column(name = "cor_do_card")
    private String cardColor;
    @NotNull
    @Column(name = "caminho_da_imagem")
    private String pathImage;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
