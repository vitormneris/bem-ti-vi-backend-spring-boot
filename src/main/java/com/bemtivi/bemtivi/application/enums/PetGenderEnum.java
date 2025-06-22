package com.bemtivi.bemtivi.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PetGenderEnum {
    MALE("Macho"),
    FEMALE("Fêmea");

    private final String description;
}
