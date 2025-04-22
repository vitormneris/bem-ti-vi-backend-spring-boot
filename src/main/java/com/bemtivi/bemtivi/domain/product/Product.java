package com.bemtivi.bemtivi.domain.product;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.category.Category;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String pathImage;
    private BigDecimal price;
    private String description;
    private Set<Category> categories;
    private ActivationStatus activationStatus;
}
