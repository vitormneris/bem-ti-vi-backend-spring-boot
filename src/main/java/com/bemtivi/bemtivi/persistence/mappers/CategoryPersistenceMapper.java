package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.category.Category;
import com.bemtivi.bemtivi.application.domain.product.Product;
import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {
    CategoryEntity mapToEntity(Category category);

    Set<Category> mapToSetCategoryDomain(Set<CategoryEntity> categories);

    @Mapping(target = "products", source = "products", qualifiedByName = "mapToProductDomain")
    Category mapToDomain(CategoryEntity categoryEntity);

    @Named("mapToProductDomain")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Product mapToProductDomain(ProductEntity productEntity);

    default PageResponse<Category> mapToPageResponseDomain(Page<CategoryEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Category> categories = new LinkedHashSet<>(pageResponse.getContent().stream().map(this::mapToDomain).toList());
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
