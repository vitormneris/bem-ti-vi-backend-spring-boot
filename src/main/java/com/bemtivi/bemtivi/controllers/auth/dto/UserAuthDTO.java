package com.bemtivi.bemtivi.controllers.auth.dto;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;

public record UserAuthDTO(
        String id,
        String name,
        String email,
        UserRoleEnum role,
        String password
) {
}
