package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.category.Category;
import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {
    CategoryEntity mapToEntity(Category category);
    Category mapToDomain(CategoryEntity categoryEntity);

    default PageResponse<Category> mapToPageResponseDomain(Page<CategoryEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Category> categories = pageResponse.getContent().stream().map(this::mapToDomain).collect(Collectors.toSet());
        return PageResponse.<Category>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(categories)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }
}
