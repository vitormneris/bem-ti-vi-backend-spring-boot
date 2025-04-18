package com.bemtivi.bemtivi.controllers.in.product.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductDTO(
        String id,
        @NotBlank(message = "O nome não pode ficar em branco.")
        @Size(groups = Mandatory.class, min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,
        String pathImage,
        @NotNull(message = "O campo não pode ficar em branco.")
        @Max(groups = Mandatory.class, value = 1_000_000_000, message = "O valor não pode ultrapasar 1 bilhão.")
        @PositiveOrZero(groups = Mandatory.class, message = "O preço não pode ser negativo.")
        BigDecimal price,
        @NotBlank(message = "A descrição não pode ficar em branco.")
        @Size(groups = Mandatory.class, max = 1500, message = "A descrição não pode ultrapassar 1500 caracteres")
        String description,
        ActivationStatusDTO activationStatus
) {
        public interface Mandatory {}
}
