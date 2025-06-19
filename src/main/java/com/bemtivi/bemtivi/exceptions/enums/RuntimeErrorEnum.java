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
    ERR0011("RESOURCE_NOT_FOUND", "O comentário solicitado não foi encontrado."),
    ERR0012("UPLOAD_OBJECT_ERROR", "Houve um erro ao fazer o upload da imagem."),
    ERR0013("RESOURCE_DUPLICATED", "Este e-mail já está registrado."),
    ERR0014("AUTHENTICATION_FAILED", "E-mail ou senha estão incorretos."),
    ERR0015("AUTHENTICATION_FAILED", "O token está expirado ou inválido."),
    ERR0016("INVALID_ARGUMENTS", "A data inserida é inválida."),
    ERR0017("OPERATION_NOT_ALLOWED", "Não é possível criar mais do que 5 administradores."),
    ERR0018("UPLOAD_OBJECT_ERROR", "O tipo do arquivo enviado não é válido. Escolha entre (JPEG, PNG, WEBP)"),
    ERR0019("PASSWORD_WRONG", "A senha digitada está incorreta."),
    ERR0020("INVALID_CODE", "O código enviado está incorreto. Refaça a solicitação."),
    ERR0021("EMAIL_IS_NOT_ACTIVE", "O e-mail precisa está ativo para realizar esta operação."),
    ERR0022("THIS_IS_THE_LAST_ADMINISTRATOR", "Não é possível realizar esta operação, pois este é o último administrador do sistema."),
    ERR0023("EMAIL_IS_ACTIVE", "Não é possível realizar esta operação, pois o e-mail já está ativo."),
    ERR0024("PASSWORD_IS_NULL", "Não é possível realizar esta operação, pois a senha é nula!!."),
    ERR0025("EMAIL_IS_NULL", "Não é possível realizar esta operação, pois o e-mail é nulo!."),
    ERR0026("ID_IS_NULL", "Não é possível realizar esta operação, pois o id é nulo!."),
    ERR0027("CODE_IS_NULL", "Não é possível realizar esta operação, pois o código é nulo!."),
    ERR0028("OPERATION_NOT_ALLOWED", "Não é possível realizar esta operação, pois tente novamente mais tarde!.");

    private final String code;
    private final String message;
}
