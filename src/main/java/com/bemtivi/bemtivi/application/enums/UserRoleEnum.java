package com.bemtivi.bemtivi.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    CUSTOMER("Cliente"),
    ADMINISTRATOR("Administrador");

    private final String description;
}
