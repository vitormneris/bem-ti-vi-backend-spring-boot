package com.bemtivi.bemtivi.persistence.entities.category;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
    @ManyToMany(mappedBy = "categories")
    private Set<ProductEntity> products;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
