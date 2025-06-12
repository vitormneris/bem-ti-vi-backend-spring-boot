package com.bemtivi.bemtivi.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeComment {
    SERVICE("servico"),
    PRODUCT("produto");

    private final String description;
}
