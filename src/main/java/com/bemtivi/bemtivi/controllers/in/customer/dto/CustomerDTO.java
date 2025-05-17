package com.bemtivi.bemtivi.controllers.in.customer.dto;

import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.appointment.dto.AppointmentDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.OrderDTO;
import com.bemtivi.bemtivi.controllers.in.pet.dto.PetDTO;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record CustomerDTO(
        String id,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O nome está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O nome está muito longo.")
        @NotBlank(groups = {OnCreate.class}, message = "O campo nome deve ser preenchido.")
        String name,
        @Email(groups = {OnCreate.class, OnUpdate.class}, message = "O e-mail está em formato inválido")
        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "O campo e-mail deve ser preenchido.")
        String email,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 8, message = "A senha está muito curta.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "A senha está muito longa.")
        @NotBlank(groups = {OnCreate.class}, message = "O campo senha deve ser preenchido.")
        String password,
        @Past(groups = {OnCreate.class, OnUpdate.class}, message = "A data de nascimento está inválida.")
        @NotNull(groups = {OnCreate.class}, message = "O campo data de nascimento deve ser preenchido.")
        LocalDate birthDate,
        String pathImage,
        @NotNull(groups = {OnCreate.class}, message = "O objeto telefone não deve ser nulo.")
        TelephoneDTO telephones,
        @NotNull(groups = {OnCreate.class}, message = "O objeto endereco não deve ser nulo.")
        AddressDTO address,
        Set<PetDTO> pets,
        Set<OrderDTO> orders,
        Set<AppointmentDTO> appointments,
        ActivationStatusDTO activationStatus
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
