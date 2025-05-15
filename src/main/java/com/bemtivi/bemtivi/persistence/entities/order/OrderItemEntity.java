package com.bemtivi.bemtivi.persistence.entities.order;

import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_item_pedido")
public class OrderItemEntity {
    @Id
    @Column(name = "pedido_item_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "preco", nullable = false)
    private BigDecimal price;
    @Column(name = "quantidade", nullable = false)
    private Integer quantity;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "produto_id", referencedColumnName = "produto_id")
    private ProductEntity product;
}
