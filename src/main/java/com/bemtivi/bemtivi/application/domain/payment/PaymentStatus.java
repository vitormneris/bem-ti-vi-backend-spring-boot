package com.bemtivi.bemtivi.application.domain.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatus {
    public Long id;
    private String status;
    private String statusDetail;

}
