package com.bemtivi.bemtivi.persistence.entities.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PixEntity {
    @NotNull
    @Column(length = 5000)
    private String qrCode;
    @NotNull
    @Column(length = 5000)
    private String qrCodeBase64;
}
