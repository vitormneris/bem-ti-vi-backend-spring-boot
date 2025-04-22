package com.bemtivi.bemtivi.domain.petservice;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PetService {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private BigDecimal price;
    private LocalTime estimated_duration;
    private String pathImage;
    private String description;
    private ActivationStatus activationStatus;
}
