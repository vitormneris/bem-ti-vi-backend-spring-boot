package com.bemtivi.bemtivi.controllers.in.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TelephoneDTO(
        String id,
        @Pattern(
                groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class},
                regexp = "^$|^(\\(?\\d{2}\\)?\\s?)?(9\\d{4}|[2-9]\\d{3})-?\\d{4}$",
                message = "1° número de telefone inválido."
        )
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O 1° campo de telefone deve ser preenchido.")
        String phoneOne,
        @Pattern(
                groups = {CustomerDTO.OnCreate.class, CustomerDTO.OnUpdate.class},
                regexp = "^$|^(\\(?\\d{2}\\)?\\s?)?(9\\d{4}|[2-9]\\d{3})-?\\d{4}$",
                message = "2° número de telefone inválido."
        )
        String phoneTwo
) {

}
