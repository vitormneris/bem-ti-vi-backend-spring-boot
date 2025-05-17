package com.bemtivi.bemtivi.application.domain.product;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.category.Category;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
