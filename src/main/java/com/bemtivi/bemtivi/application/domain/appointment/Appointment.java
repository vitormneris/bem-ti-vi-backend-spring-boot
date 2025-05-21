package com.bemtivi.bemtivi.application.domain.appointment;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.service.Service;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Appointment {
    @EqualsAndHashCode.Include
    private String id;
    private Instant moment;
    private Customer customer;
    private BigDecimal price;
    private PaymentStatusEnum paymentStatus;
    private Service service;
    private ActivationStatus activationStatus;
}
