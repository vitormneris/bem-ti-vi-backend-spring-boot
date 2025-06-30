package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.comment.Comment;
import com.bemtivi.bemtivi.application.domain.product.Product;
import com.bemtivi.bemtivi.application.domain.service.Service;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.persistence.entities.jpa.comment.CommentEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.product.ProductEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.service.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.LinkedHashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CommentPersistenceMapper {
    CommentEntity mapToEntity(Comment appointment);

    @Mapping(target = "service", source = "service", qualifiedByName = "mapToServiceDomain")
    @Mapping(target = "product", source = "product", qualifiedByName = "mapToProductDomain")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "mapToCustomerDomain")
    Comment mapToDomain(CommentEntity appointment);

    @Named("mapToCustomerDomain")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(source = "birthDate", target = "birthDate", ignore = true)
    @Mapping(source = "telephones", target = "telephones", ignore = true)
    @Mapping(source = "address", target = "address", ignore = true)
    @Mapping(source = "role", target = "role", ignore = true)
    @Mapping(source = "password", target = "password", ignore = true)
    @Mapping(target = "activationStatus", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Customer mapToCustomerDomain(CustomerEntity customerEntity);

    @Named("mapToServiceDomain")
    @Mapping(target = "pathImage", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "activationStatus", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Service mapToServiceDomain(ServiceEntity serviceEntity);

    @Named("mapToProductDomain")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "pathImage", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "activationStatus", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Product mapToProductDomain(ProductEntity productEntity);

    default PageResponse<Comment> mapToPageResponseDomain(Page<CommentEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Comment> appointments = new LinkedHashSet<>(pageResponse.getContent().stream().map(this::mapToDomain).toList());
        return PageResponse.<Comment>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(appointments)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }
}
