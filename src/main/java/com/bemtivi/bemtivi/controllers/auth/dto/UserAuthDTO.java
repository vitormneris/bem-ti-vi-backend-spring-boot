package com.bemtivi.bemtivi.controllers.auth.dto;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserAuthDTO(
        String id,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O nome está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "O nome está muito longo.")
        @NotNull(groups = {OnCreate.class}, message = "O campo nome deve ser preenchido.")
        String name,
        @Email(groups = {OnCreate.class, OnUpdate.class}, message = "O e-mail está em formato inválido")
        @NotBlank(groups = {OnCreate.class}, message = "O campo e-mail deve ser preenchido.")
        String email,
        Boolean isEmailActive,
        UserRoleEnum role,
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 8, message = "A senha está muito curta.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 100, message = "A senha está muito longa.")
        @NotNull(groups = {OnCreate.class}, message = "O campo senha deve ser preenchido.")
        String password
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
