package com.bemtivi.bemtivi.controllers.in.administrator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PasswordsDTO(
        @NotNull(groups = {OnCreate.class}, message = "O campo senha antiga deve ser preenchido.")
        String passwordOld,
        @Size(groups = {OnCreate.class}, min = 8, message = "A senha está muito curta.")
        @Size(groups = {OnCreate.class}, max = 100, message = "A senha está muito longa.")
        @NotNull(groups = {OnCreate.class}, message = "O campo senha nova deve ser preenchido.")
        String passwordNew
) {
    public interface OnCreate {}
}
