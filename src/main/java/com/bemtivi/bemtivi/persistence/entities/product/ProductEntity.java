package com.bemtivi.bemtivi.persistence.entities.product;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_produtos")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "produto_id")
    private String id;
    @NotNull
    @Column(name = "nome")
    private String name;
    @NotNull
    @Column(name = "caminho_da_imagem")
    private String pathImage;
    @NotNull
    @Column(name = "preco")
    private BigDecimal price;
    @NotNull
    @Column(name = "descricao")
    private String description;
    @NotNull
    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<CategoryEntity> categories;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
