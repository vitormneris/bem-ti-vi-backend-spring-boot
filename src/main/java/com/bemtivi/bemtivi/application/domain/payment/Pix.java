package com.bemtivi.bemtivi.application.domain.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pix {
    private String qrCode;
    private String qrCodeBase64;
}