package com.bemtivi.bemtivi.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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
    @Column(name = "imagem")
    private String pathImage;
    @NotNull
    @Column(name = "preco")
    private BigDecimal price;
    @NotNull
    @Column(name = "descricao")
    private String description;
}
