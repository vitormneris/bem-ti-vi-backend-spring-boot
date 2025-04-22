package com.bemtivi.bemtivi.controllers.in.category.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import jakarta.validation.constraints.*;

import java.util.Set;

public record CategoryDTO(
        String id,
        @NotBlank(groups = {OnCreate.class}, message = "O nome deve ser preenchido.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
        String name,
        String pathImage,
        @NotBlank(groups = {OnCreate.class}, message = "A cor do card deve ser preenchida.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 4,  max = 9, message = "O formato da cor deve ser #RRGGBBOO")
        String cardColor,
        Set<ProductDTO> products,
        ActivationStatusDTO activationStatus
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
