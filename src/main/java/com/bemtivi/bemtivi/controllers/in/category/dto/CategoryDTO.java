package com.bemtivi.bemtivi.controllers.in.category.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;

public record CategoryDTO(
        String id,
        @NotBlank(groups = {Default.class}, message = "O nome deve ser preenchido.")
        @Size(groups = {Default.class, Mandatory.class}, min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
        String name,
        String pathImage,
        @NotBlank(groups = {Default.class}, message = "A cor do card deve ser preenchida.")
        @Size(groups = {Default.class, Mandatory.class}, min = 4,  max = 9, message = "O formato da cor deve ser #RRGGBBOO")
        String cardColor,
        ActivationStatusDTO activationStatus
) {
        public interface Mandatory {}
}
