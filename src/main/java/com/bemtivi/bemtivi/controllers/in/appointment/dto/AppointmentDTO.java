package com.bemtivi.bemtivi.controllers.in.appointment.dto;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.PixDTO;
import com.bemtivi.bemtivi.controllers.in.pet.dto.PetDTO;
import com.bemtivi.bemtivi.controllers.in.service.dto.ServiceDTO;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentDTO(
        String id,
        @Future(groups = {OnCreate.class, OnUpdate.class}, message = "O campo data está inválido.")
        @NotNull(groups = {OnCreate.class}, message = "O campo data deve ser preenchido.")
        LocalDateTime dateTime,
        @NotNull(groups = {OnCreate.class}, message = "O campo cliente não deve ser nulo.")
        CustomerDTO customer,
        BigDecimal price,
        Long paymentId,
        PaymentStatusEnum paymentStatus,
        @NotNull(groups = {OnCreate.class}, message = "O campo serviço não deve ser nulo.")
        ServiceDTO service,
        @NotNull(groups = {OnCreate.class}, message = "O campo pet não deve ser nulo.")
        PetDTO pet,
        @NotNull(groups = {OnCreate.class}, message = "O campo método de pagamento deve ser preenchido.")
        Boolean methodPaymentByPix,
        PixDTO pix,
        ActivationStatus activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
