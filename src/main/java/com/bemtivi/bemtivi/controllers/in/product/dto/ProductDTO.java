package com.bemtivi.bemtivi.controllers.in.product.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.category.dto.CategoryDTO;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record ProductDTO(
        String id,
        @NotBlank(groups = {OnCreate.class}, message = "O nome deve ser preenchido.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,
        String pathImage,
        @NotNull(groups = {OnCreate.class}, message = "O preço deve ser preenchido.")
        @Max(groups = {OnCreate.class, OnUpdate.class}, value = 1_000_000_000, message = "O valor não pode ultrapasar 1 bilhão.")
        @PositiveOrZero(groups = {OnCreate.class, OnUpdate.class}, message = "O preço não pode ser negativo.")
        BigDecimal price,
        @NotBlank(groups = {OnCreate.class}, message = "A descrição deve ser preenchida.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 1500, message = "A descrição não pode ultrapassar 1500 caracteres")
        String description,
        @NotNull(groups = {CategoryDTO.OnCreate.class}, message = "O campo categoria deve ser preenchido.")
        @Size(groups = {CategoryDTO.OnCreate.class, CategoryDTO.OnUpdate.class}, min = 1, message = "Ao menos uma categoria deve ser selecionada.")
        Set<CategoryDTO> categories,
        ActivationStatusDTO activationStatus
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
