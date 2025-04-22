package com.bemtivi.bemtivi.exceptions;

import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import lombok.Getter;

@Getter
public class InternalErrorException extends RuntimeException {
    private final RuntimeErrorEnum errorEnum;

    public InternalErrorException(RuntimeErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.errorEnum = errorEnum;
    }
}
