package com.bemtivi.bemtivi.controllers.exceptionhandler.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ErrorMessageDTO(
        String code,
        int status,
        String message,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT-3")
        Instant timestamp,
        String path,
        List<ErrorFieldDTO> errorFields
) {
}
