package com.bemtivi.bemtivi.controllers.exceptionhandler.dto;

import lombok.Builder;

@Builder
public record ErrorFieldDTO(
        String name,
        String description,
        String value
) {

}
