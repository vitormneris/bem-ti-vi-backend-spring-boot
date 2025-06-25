package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.category.Category;
import com.bemtivi.bemtivi.application.domain.product.Product;
import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.mapstruct.Mapper;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    ProductEntity mapToEntity(Product product);

    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapToCategoryDomain")
    @Mapping(target = "comments", ignore = true)
    Product mapToDomain(ProductEntity productEntity);

    @Named("mapToCategoryDomain")
    @Mapping(target = "products", ignore = true)
    Category mapToCategoryDomain(CategoryEntity categoryEntity);

    Set<CategoryEntity> mapToSetCategoryEntity(Set<Category> categories);

    default PageResponse<Product> mapToPageResponseDomain(Page<ProductEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Product> products = new LinkedHashSet<>(pageResponse.getContent().stream().map(this::mapToDomain).toList());
        return PageResponse.<Product>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(products)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }

}
