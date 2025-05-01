package com.bemtivi.bemtivi.controllers.in.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record AddressDTO(
        String id,
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo estado deve ser preenchido.")
        String state,
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo cidade deve ser preenchido.")
        String city,
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo bairro deve ser preenchido.")
        String neighborhood,
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo rua deve ser preenchido.")
        String street,
        @PositiveOrZero(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, message = "O número da casa está inválido.")
        @NotNull(groups = {CustomerDTO.OnCreate.class}, message = "O campo número deve ser preenchido.")
        Integer number,
        String complement,
        @Pattern(groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class}, regexp = "^[0-9]{5}-?[0-9]{3}$")
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo CEP deve ser preenchido.")
        String postalCode,
        CustomerDTO customer
) {

}
