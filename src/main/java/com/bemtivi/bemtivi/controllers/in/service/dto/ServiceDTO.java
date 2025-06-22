package com.bemtivi.bemtivi.controllers.in.service.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.AdministratorDTO;
import com.bemtivi.bemtivi.controllers.in.category.dto.CategoryDTO;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalTime;

public record ServiceDTO(
        String id,
        @NotNull(groups = {OnCreate.class}, message = "O campo nome deve ser preenchido.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O campo nome está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O campo nome está muito longo.")
        String name,
        String pathImage,
        @NotNull(groups = {OnCreate.class}, message = "O campo preço deve ser preenchido.")
        @Max(groups = {OnCreate.class, OnUpdate.class}, value = 1_000_000_000, message = "O campo preço não pode ultrapassar 1 bilhão.")
        @PositiveOrZero(groups = {OnCreate.class, OnUpdate.class}, message = "O campo preço não pode ser negativo.")
        BigDecimal price,
        @NotBlank(groups = {OnCreate.class}, message = "O campo descrição deve ser preenchido.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 1500, message = "O campo descrição não pode ultrapassar 1500 caracteres.")
        String description,
        Double rate,
        @NotNull(groups = {OnCreate.class}, message = "O campo duração estimada deve ser preenchido.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime estimatedDuration,
        ActivationStatusDTO activationStatus
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}