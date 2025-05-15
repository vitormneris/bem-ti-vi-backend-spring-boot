package com.bemtivi.bemtivi.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatusEnum {
    WAITING_PAYMENT(1, "Esperando pagamento"),
    PAID(2, "Pago"),
    SHIPPED(3, "Enviado"),
    DELIVERED(4, "Entregue"),
    CANCELED(5, "Cancelado");

    private final int code;
    private final String message;
}
