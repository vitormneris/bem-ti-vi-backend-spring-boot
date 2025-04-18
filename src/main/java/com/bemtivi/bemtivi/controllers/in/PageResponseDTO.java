package com.bemtivi.bemtivi.controllers.in;

import java.util.Set;

public record PageResponseDTO<T>(
        long pageSize,
        int totalElements,
        int totalPages,
        int currentPage,
        int nextPage,
        int previousPage,
        Set<T> content
) {
}
