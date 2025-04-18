package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.product.Product;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    Product mapToDomain(ProductEntity productEntity);
    ProductEntity mapToEntity(Product product);
    ActivationStatusEntity mapToEntity(ActivationStatus activationStatus);
    ActivationStatus mapToDomain(ActivationStatusEntity activationStatus);

    default PageResponse<Product> mapToPageResponseDomain(Page<ProductEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Product> products = pageResponse.getContent().stream().map(this::mapToDomain).collect(Collectors.toSet());
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
