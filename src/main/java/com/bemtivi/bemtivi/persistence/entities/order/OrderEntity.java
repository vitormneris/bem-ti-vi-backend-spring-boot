package com.bemtivi.bemtivi.persistence.entities.order;

import com.bemtivi.bemtivi.domain.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_pedidos")
public class OrderEntity {
    @Id
    @Column(name = "pedido_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "momento", nullable = false)
    private Instant moment;
    @Column(name = "status_pagamento", nullable = false)
    private PaymentStatusEnum paymentStatus;
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "pedido_item_id")
    private Set<OrderItemEntity> orderItems;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
