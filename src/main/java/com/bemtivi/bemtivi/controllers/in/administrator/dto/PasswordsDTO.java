package com.bemtivi.bemtivi.controllers.in.administrator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PasswordsDTO(
        @NotNull(groups = {OnCreate.class}, message = "O campo senha antiga deve ser preenchido.")
        String passwordOld,
        @Size(groups = {OnCreate.class}, min = 8, message = "O campo senha nova está muito curto.")
        @Size(groups = {OnCreate.class}, max = 100, message = "O campo senha nova está muito longo.")
        @NotNull(groups = {OnCreate.class}, message = "O campo senha nova deve ser preenchido.")
        String passwordNew
) {
    public interface OnCreate {}
}
