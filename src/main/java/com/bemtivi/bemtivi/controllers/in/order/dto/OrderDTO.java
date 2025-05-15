package com.bemtivi.bemtivi.controllers.in.order.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.domain.enums.PaymentStatusEnum;

import java.time.Instant;
import java.util.Set;

public record OrderDTO(
        String id,
        Instant moment,
        PaymentStatusEnum paymentStatus,
        Set<OrderItemDTO> orderItemDTOS,
        ActivationStatusDTO activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
