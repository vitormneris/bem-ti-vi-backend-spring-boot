package com.bemtivi.bemtivi.controllers.exceptionhandler;

import com.bemtivi.bemtivi.controllers.exceptionhandler.dto.ErrorFieldDTO;
import com.bemtivi.bemtivi.controllers.exceptionhandler.dto.ErrorMessageDTO;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.DuplicateResourceException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerResource {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> resourceNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        RuntimeErrorEnum runtimeErrorEnum = exception.getError();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .code(runtimeErrorEnum.getCode())
                .status(httpStatus.value())
                .message(runtimeErrorEnum.getMessage())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDTO> methodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        RuntimeErrorEnum errorEnum = RuntimeErrorEnum.ERR0001;
        List<ErrorFieldDTO> fields = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            ErrorFieldDTO errorFieldDTO = ErrorFieldDTO.builder()
                    .name(fieldError.getField())
                    .description(fieldError.getDefaultMessage())
                    .value(String.valueOf(fieldError.getRejectedValue()))
                    .build();

            fields.add(errorFieldDTO);
        });

        ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .code(errorEnum.getCode())
                .status(httpStatus.value())
                .message(errorEnum.getMessage())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .errorFields(fields)
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageDTO);
    }

    @ExceptionHandler(DatabaseIntegrityViolationException.class)
    public ResponseEntity<ErrorMessageDTO> dataIntegrityViolation(DatabaseIntegrityViolationException exception, HttpServletRequest request) {
        RuntimeErrorEnum runtimeErrorEnum = exception.getError();
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .code(runtimeErrorEnum.getCode())
                .status(httpStatus.value())
                .message(runtimeErrorEnum.getMessage())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageDTO);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorMessageDTO> duplicateResource(DuplicateResourceException exception, HttpServletRequest request) {
        RuntimeErrorEnum runtimeErrorEnum = exception.getError();
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .code(runtimeErrorEnum.getCode())
                .status(httpStatus.value())
                .message(runtimeErrorEnum.getMessage())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageDTO);
    }
}
