package com.bemtivi.bemtivi.application.domain.service;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Service {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private BigDecimal price;
    private LocalTime estimatedDuration;
    private String pathImage;
    private String description;
    private ActivationStatus activationStatus;
}
