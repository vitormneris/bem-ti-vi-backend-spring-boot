package com.bemtivi.bemtivi.domain.category;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.product.Product;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String cardColor;
    private String pathImage;
    private Set<Product> products;
    private ActivationStatus activationStatus;
}
