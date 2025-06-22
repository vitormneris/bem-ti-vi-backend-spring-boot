package com.bemtivi.bemtivi.controllers.in.customer.dto;

import jakarta.validation.constraints.*;

public record AddressDTO(
        String id,
        @Size(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, max = 100, message = "O campo estado está muito longo.")
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo estado deve ser preenchido.")
        String state,
        @Size(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, max = 100, message = "O campo cidade está muito longo.")
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo cidade deve ser preenchido.")
        String city,
        @Size(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, max = 100, message = "O campo bairro está muito longo.")
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo bairro deve ser preenchido.")
        String neighborhood,
        @Size(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, max = 100, message = "O campo rua está muito longo.")
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo rua deve ser preenchido.")
        String street,
        @Max(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, value = 1_000_000, message = "O campo número da residência está muito alto.")
        @PositiveOrZero(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, message = "O campo número da casa está inválido.")
        @NotNull(groups = {CustomerDTO.OnCreate.class}, message = "O campo número deve ser preenchido.")
        String number,
        @Size(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, max = 150, message = "O campo complemento está muito longo.")
        String complement,
        @Pattern(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, regexp = "^[0-9]{5}-?[0-9]{3}$", message = "O campo CEP está inválido")
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo CEP deve ser preenchido.")
        String postalCode
) {

}
