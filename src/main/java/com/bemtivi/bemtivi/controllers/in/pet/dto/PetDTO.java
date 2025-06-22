package com.bemtivi.bemtivi.controllers.in.pet.dto;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.application.enums.PetGenderEnum;
import com.bemtivi.bemtivi.application.enums.PetSizeEnum;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PetDTO(
        String id,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O campo nome está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O campo nome está muito longo.")
        @NotBlank(groups = {OnCreate.class}, message = "O campo nome deve ser preenchido.")
        String name,
        @Past(groups = {OnCreate.class, OnUpdate.class}, message = "O campo data de nascimento está inválido.")
        @NotNull(groups = {OnCreate.class}, message = "O campo data de nascimento deve ser preenchido.")
        LocalDate birthDate,
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 50, message = "O campo raça está muito longo.")
        @NotBlank(groups = {OnCreate.class}, message = "O campo raça deve ser preenchido.")
        String race,
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 20, message = "O campo porte está muito longo.")
        @NotBlank(groups = {OnCreate.class}, message = "O campo porte deve ser selecionado.")
        PetSizeEnum size,
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 20, message = "O campo genero está muito longo.")
        @NotBlank(groups = {OnCreate.class}, message = "O campo genero deve ser selecionado.")
        PetGenderEnum gender,
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 50, message = "O campo espécie está muito longo.")
        @NotBlank(groups = {OnCreate.class}, message = "O campo espécie deve ser preenchido.")
        String species,
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 700, message = "O campo detalhe está muito longo.")
        String details,
        String pathImage,
        @NotNull(groups = {OnCreate.class}, message = "O campo dono precisa ser enviado.")
        CustomerDTO owner,
        ActivationStatusDTO activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
