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
    ERR0006("RESOURCE_NOT_FOUND", "O cliente solicitado não foi encontrado."),
    ERR0007("RESOURCE_NOT_FOUND", "O pet solicitado não foi encontrado."),
    ERR0008("RESOURCE_NOT_FOUND", "O pedido solicitado não foi encontrado."),
    ERR0009("RESOURCE_NOT_FOUND", "O agendamento solicitado não foi encontrado."),
    ERR0010("RESOURCE_NOT_FOUND", "O administrador solicitado não foi encontrado."),
    ERR0011("RESOURCE_NOT_FOUND", "O e-mail solicitado não foi encontrado."),
    ERR0012("UPLOAD_OBJECT_ERROR", "Houve um erro ao fazer o upload da imagem."),
    ERR0013("RESOURCE_DUPLICATED", "Este e-mail já está registrado."),
    ERR0014("AUTHENTICATION_FAILED", "E-mail ou senha estão incorretos.");

    private final String code;
    private final String message;
}
