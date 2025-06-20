package com.bemtivi.bemtivi.application.domain.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private Long id;
    private String status;
    private String statusDetail;
    private String description;
    private String paymentMethod;
    private Pix pix;
}

