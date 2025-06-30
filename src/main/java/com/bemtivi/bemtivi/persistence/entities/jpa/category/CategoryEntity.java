package com.bemtivi.bemtivi.persistence.entities.jpa.category;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_categorias")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "categoria_id")
    private String id;
    @Column(name = "nome", nullable = false)
    private String name;
    @Column(name = "cor_do_card", nullable = false)
    private String cardColor;
    @Column(name = "caminho_da_imagem", nullable = false)
    private String pathImage;
    @ManyToMany(cascade = CascadeType.REMOVE, mappedBy = "categories")
    private Set<ProductEntity> products;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
