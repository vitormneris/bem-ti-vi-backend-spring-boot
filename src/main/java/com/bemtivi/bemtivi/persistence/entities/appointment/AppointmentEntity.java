package com.bemtivi.bemtivi.persistence.entities.appointment;

import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.service.ServiceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_agendamentos")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "agendamento_id")
    private String id;
    @Column(name = "momento", nullable = false)
    private Instant moment;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "cliente_id")
    private CustomerEntity customer;
    @Column(name = "preco", nullable = false)
    private BigDecimal price;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status_de_pagamento", nullable = false)
    private PaymentStatusEnum paymentStatus;
    @ManyToOne
    @JoinColumn(name = "servico_id", referencedColumnName = "servico_id")
    private ServiceEntity service;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
