package com.bemtivi.bemtivi.controllers.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductDTO(
        String id,
        @NotBlank(message = "O nome não pode ficar em branco.")
        String name,
        String pathImage,
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        BigDecimal price,
        @NotBlank(message = "A descrição não pode ficar em branco.")
        String description
) {
}
