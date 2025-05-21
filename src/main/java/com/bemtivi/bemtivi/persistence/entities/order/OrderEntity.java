package com.bemtivi.bemtivi.persistence.entities.order;

import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@ToString
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
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "cliente_id")
    private CustomerEntity customer;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false)
    private PaymentStatusEnum paymentStatus;
    @Column(name = "preco_total", nullable = false)
    private BigDecimal totalPrice;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    private Set<OrderItemEntity> orderItems;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
