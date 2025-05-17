package com.bemtivi.bemtivi.controllers.in.order.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record OrderDTO(
        String id,
        Instant moment,
        PaymentStatusEnum paymentStatus,
        BigDecimal totalPrice,
        CustomerDTO customer,
        Set<OrderItemDTO> orderItems,
        ActivationStatusDTO activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
