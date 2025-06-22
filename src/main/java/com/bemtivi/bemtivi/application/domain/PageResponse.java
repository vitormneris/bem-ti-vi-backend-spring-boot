package com.bemtivi.bemtivi.application.domain;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer nextPage;
    private Integer previousPage;
    private Set<T> content;
    private Double totalRate;
}
