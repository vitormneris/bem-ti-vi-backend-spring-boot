package com.bemtivi.bemtivi.configuration.auth.dto;

import com.bemtivi.bemtivi.controllers.exceptionhandler.dto.ErrorFieldDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Builder
public record ErrorSecurityMessageDTO(
        String code,
        int status,
        String message,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT-3")
        Date timestamp,
        String path
) {

}
