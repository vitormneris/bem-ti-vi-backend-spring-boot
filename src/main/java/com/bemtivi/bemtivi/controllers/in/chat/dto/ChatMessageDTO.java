package com.bemtivi.bemtivi.controllers.in.chat.dto;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ChatMessageDTO(
        String id,
        @NotBlank(message = "O campo id do usuário deve ser preenchido.")
        String userId,
        String userName,
        @NotNull(message = "O campo enviador deve ser preenchido.")
        UserRoleEnum sender,
        @NotBlank(message = "O campo conteúdo deve ser preenchido.")
        String content,
        Instant moment
) {

}
