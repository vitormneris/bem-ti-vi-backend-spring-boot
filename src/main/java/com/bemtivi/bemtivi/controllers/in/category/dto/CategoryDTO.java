package com.bemtivi.bemtivi.controllers.in.category.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.AdministratorDTO;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import jakarta.validation.constraints.*;

import java.util.Set;

public record CategoryDTO(
        String id,
        @NotNull(groups = {OnCreate.class}, message = "O nome deve ser preenchido.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O nome está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 50, message = "O nome está muito longo.")
        String name,
        String pathImage,
        @NotNull(groups = {OnCreate.class}, message = "A cor do card deve ser preenchida.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 4,  max = 9, message = "O formato da cor deve ser #RRGGBBOO")
        String cardColor,
        Set<ProductDTO> products,
        ActivationStatusDTO activationStatus
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
