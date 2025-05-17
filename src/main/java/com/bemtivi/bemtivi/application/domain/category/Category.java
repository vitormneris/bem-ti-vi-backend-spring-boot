package com.bemtivi.bemtivi.application.domain.category;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.product.Product;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
