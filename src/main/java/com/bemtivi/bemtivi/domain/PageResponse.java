package com.bemtivi.bemtivi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class PageResponse<T> {
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer nextPage;
    private Integer previousPage;
    private Set<T> content;
}
