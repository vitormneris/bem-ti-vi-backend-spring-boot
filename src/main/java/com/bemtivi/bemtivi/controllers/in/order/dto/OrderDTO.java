package com.bemtivi.bemtivi.controllers.in.order.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.appointment.dto.AppointmentDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDTO(
        String id,
        Instant moment,
        PaymentStatusEnum paymentStatus,
        BigDecimal totalPrice,
        Long paymentId,
        @NotNull(groups = {OnCreate.class}, message = "O campo cliente não deve ser nulo.")
        CustomerDTO customer,
        @NotNull(groups = {OnCreate.class}, message = "O campo item de pedido não deve ser nulo.")
        List<OrderItemDTO> orderItems,
        PixDTO pix,
        ActivationStatusDTO activationStatus
) {
    public interface OnCreate {}
}
