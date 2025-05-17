package com.bemtivi.bemtivi.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PetGenderEnum {
    MASCULINE("Masculino"),
    FEMININE("Feminino");

    private final String description;
}
