package com.bemtivi.bemtivi.domain.product;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String pathImage;
    private BigDecimal price;
    private String description;
    private ActivationStatus activationStatus;
}
