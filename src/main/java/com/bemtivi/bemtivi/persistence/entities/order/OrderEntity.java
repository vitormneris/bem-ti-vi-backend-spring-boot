package com.bemtivi.bemtivi.persistence.entities.order;

import com.bemtivi.bemtivi.application.domain.payment.PaymentResponse;
import com.bemtivi.bemtivi.application.domain.payment.Pix;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.payment.PixEntity;
import com.mercadopago.resources.payment.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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
    @Column(name = "pagamento_id", nullable = false)
    private Long paymentId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    private List<OrderItemEntity> orderItems;
    @Embedded
    private PixEntity pix;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
