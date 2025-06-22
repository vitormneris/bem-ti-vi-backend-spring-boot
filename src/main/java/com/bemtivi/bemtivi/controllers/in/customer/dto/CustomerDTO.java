package com.bemtivi.bemtivi.controllers.in.customer.dto;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.appointment.dto.AppointmentDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.OrderDTO;
import com.bemtivi.bemtivi.controllers.in.pet.dto.PetDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Set;

public record CustomerDTO(
        String id,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O campo nome está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O campo nome está muito longo.")
        @NotNull(groups = {OnCreate.class}, message = "O campo nome deve ser preenchido.")
        String name,
        @Email(groups = {OnCreate.class, OnUpdate.class}, message = "O campo e-mail está em formato inválido")
        @NotBlank(groups = {OnCreate.class}, message = "O campo e-mail deve ser preenchido.")
        String email,
        @CPF(groups = {OnCreate.class, OnUpdate.class}, message = "O campo CPF está em formato inválido")
        @NotBlank(groups = {OnCreate.class}, message = "O campo cpf deve ser preenchido.")
        String cpf,
        Boolean isEmailActive,
        UserRoleEnum role,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 8, message = "O campo senha está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O campo  senha está muito longo.")
        @NotNull(groups = {OnCreate.class}, message = "O campo senha deve ser preenchido.")
        String password,
        @Past(groups = {OnCreate.class, OnUpdate.class}, message = "O campo data de nascimento está inválido.")
        @NotNull(groups = {OnCreate.class}, message = "O campo data de nascimento deve ser preenchido.")
        LocalDate birthDate,
        String pathImage,
        @Valid
        @NotNull(groups = {OnCreate.class}, message = "O campo telefones não deve ser nulo.")
        TelephoneDTO telephones,
        @Valid
        @NotNull(groups = {OnCreate.class}, message = "O campo endereco não deve ser nulo.")
        AddressDTO address,
        Set<PetDTO> pets,
        Set<OrderDTO> orders,
        Set<AppointmentDTO> appointments,
        ActivationStatusDTO activationStatus
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
