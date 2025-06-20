package com.bemtivi.bemtivi.application.domain.appointment;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.payment.Pix;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.service.Service;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Appointment {
    @EqualsAndHashCode.Include
    private String id;
    private LocalDateTime dateTime;
    private Customer customer;
    private BigDecimal price;
    private Long paymentId;
    private PaymentStatusEnum paymentStatus;
    private Service service;
    private Pix pix;
    private ActivationStatus activationStatus;
}
