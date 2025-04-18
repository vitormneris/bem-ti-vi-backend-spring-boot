package com.bemtivi.bemtivi.exceptions.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RuntimeErrorEnum {
    ERR0001("RESOURCE_NOT_FOUND", "Este recurso não foi encontrado."),
    ERR0002("INVALID_ARGUMENTS", "Existem campos inválidos na solicitação."),
    ERR0003("DATA_INTEGRITY_VIOLATION", "Houve um erro na operação com o banco de dados.");

    private final String code;
    private final String message;
}
