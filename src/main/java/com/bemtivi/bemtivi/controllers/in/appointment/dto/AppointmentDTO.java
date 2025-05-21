package com.bemtivi.bemtivi.controllers.in.appointment.dto;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.service.Service;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;

public record AppointmentDTO(
        String id,
        Instant moment,
        Customer customer,
        BigDecimal price,
        PaymentStatusEnum paymentStatus,
        Service service,
        ActivationStatus activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
