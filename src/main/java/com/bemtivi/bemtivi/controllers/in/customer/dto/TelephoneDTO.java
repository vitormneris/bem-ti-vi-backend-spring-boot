package com.bemtivi.bemtivi.controllers.in.customer.dto;

import com.bemtivi.bemtivi.domain.customer.Customer;
import jakarta.validation.constraints.NotBlank;

public record TelephoneDTO(
        String id,
        @NotBlank(groups = {CustomerDTO.OnCreate.class}, message = "O campo telefone 1 deve ser preenchido.")
        String phoneOne,
        String phoneTwo,
        Customer customer
) {

}
