package com.bemtivi.bemtivi.controllers.in.appointment.dto;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.service.Service;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.controllers.in.order.dto.PixDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record AppointmentDTO(
        String id,
        LocalDateTime dateTime,
        Customer customer,
        BigDecimal price,
        Long paymentId,
        PaymentStatusEnum paymentStatus,
        Service service,
        PixDTO pix,
        ActivationStatus activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
