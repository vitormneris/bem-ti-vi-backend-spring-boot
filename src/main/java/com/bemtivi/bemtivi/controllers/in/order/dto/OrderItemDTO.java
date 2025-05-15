package com.bemtivi.bemtivi.controllers.in.order.dto;

import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import com.bemtivi.bemtivi.domain.product.Product;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemDTO(
        String id,
        BigDecimal price,
        @NotNull(groups = {ProductDTO.OnCreate.class}, message = "A quantidade deve ser preenchida.")
        Integer quantity,
        @NotNull(groups = {ProductDTO.OnCreate.class}, message = "A produto deve ser preenchido.")
        Product product
) {

}
