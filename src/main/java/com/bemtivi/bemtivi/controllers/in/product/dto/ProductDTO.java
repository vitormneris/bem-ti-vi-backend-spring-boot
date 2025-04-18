package com.bemtivi.bemtivi.controllers.in.product.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;

import java.math.BigDecimal;

public record ProductDTO(
        String id,
        @NotBlank(groups = {Default.class}, message = "O nome deve ser preenchido.")
        @Size(groups = {Default.class, Mandatory.class}, min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,
        String pathImage,
        @NotNull(groups = {Default.class}, message = "O preço deve ser preenchido.")
        @Max(groups = {Default.class, Mandatory.class}, value = 1_000_000_000, message = "O valor não pode ultrapasar 1 bilhão.")
        @PositiveOrZero(groups = {Default.class, Mandatory.class}, message = "O preço não pode ser negativo.")
        BigDecimal price,
        @NotBlank(groups = {Default.class}, message = "A descrição deve ser preenchida.")
        @Size(groups = {Default.class, Mandatory.class}, max = 1500, message = "A descrição não pode ultrapassar 1500 caracteres")
        String description,
        ActivationStatusDTO activationStatus
) {
        public interface Mandatory {}
}
