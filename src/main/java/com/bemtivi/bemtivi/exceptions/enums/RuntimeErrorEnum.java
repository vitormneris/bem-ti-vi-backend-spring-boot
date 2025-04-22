package com.bemtivi.bemtivi.exceptions.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RuntimeErrorEnum {
    ERR0001("INVALID_ARGUMENTS", "Existem campos inválidos na solicitação."),
    ERR0002("DATA_INTEGRITY_VIOLATION", "Houve um erro na operação com o banco de dados."),
    ERR0003("RESOURCE_NOT_FOUND", "O produto solicitado não foi encontrado."),
    ERR0004("RESOURCE_NOT_FOUND", "A categoria solicitada não foi encontrada."),
    ERR0005("RESOURCE_NOT_FOUND", "O servico solicitado não foi encontrado."),
    ERR0006("UPLOAD_OBJECT_ERROR", "Houve um erro ao fazer o upload da imagem.");

    private final String code;
    private final String message;
}
