package com.bemtivi.bemtivi.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PetSizeEnum {
    SMALL("Pequeno"),
    MEDIUM("Médio"),
    LARGE("Grande");

    private final String description;
}
