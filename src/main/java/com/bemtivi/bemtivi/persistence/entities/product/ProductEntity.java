package com.bemtivi.bemtivi.persistence.entities.product;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_produtos")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "produto_id")
    private String id;
    @Column(name = "nome", nullable = false)
    private String name;
    @Column(name = "caminho_da_imagem", nullable = false)
    private String pathImage;
    @Column(name = "preco", nullable = false)
    private BigDecimal price;
    @Column(name = "descricao", nullable = false)
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
