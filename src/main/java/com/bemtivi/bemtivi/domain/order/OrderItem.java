package com.bemtivi.bemtivi.domain.order;

import com.bemtivi.bemtivi.domain.product.Product;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem {
    @EqualsAndHashCode.Include
    private String id;
    private BigDecimal price;
    private Integer quantity;
    private Product product;
}
