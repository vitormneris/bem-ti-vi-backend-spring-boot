package com.bemtivi.bemtivi.controllers.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record ActivationStatusDTO(
        Boolean isActive,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GTM-3")
        Instant creationDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GTM-3")
        Instant deactivationDate
) {

}
