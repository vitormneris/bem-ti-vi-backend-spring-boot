package com.bemtivi.bemtivi.controllers.in.pet.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.application.enums.PetGenderEnum;
import com.bemtivi.bemtivi.application.enums.PetSizeEnum;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PetDTO(
        String id,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O nome está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O nome está muito longo.")
        @NotBlank(groups = {OnCreate.class}, message = "O campo nome deve ser preenchido.")
        String name,
        @Past(groups = {OnCreate.class, OnUpdate.class}, message = "A data de nascimento está inválida.")
        @NotNull(groups = {OnCreate.class}, message = "O campo data de nascimento deve ser preenchido.")
        LocalDate birthDate,
        @NotBlank(groups = {OnCreate.class}, message = "O campo raça deve ser preenchido.")
        String race,
        @NotBlank(groups = {OnCreate.class}, message = "O campo porte deve ser selecionado.")
        PetSizeEnum size,
        @NotBlank(groups = {OnCreate.class}, message = "O campo genero deve ser selecionado.")
        PetGenderEnum gender,
        @NotBlank(groups = {OnCreate.class}, message = "O campo espécie deve ser preenchido.")
        String species,
        String note,
        String pathImage,
        @NotNull(groups = {OnCreate.class}, message = "O pet precisa ter um dono.")
        CustomerDTO owner,
        ActivationStatusDTO activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
