package com.bemtivi.bemtivi.persistence.entities.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PixEntity {
    @Lob
    private String qrCode;
    @Lob
    private String qrCodeBase64;
}
