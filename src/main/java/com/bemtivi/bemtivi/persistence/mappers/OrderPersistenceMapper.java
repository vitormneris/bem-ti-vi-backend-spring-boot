package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.order.Order;
import com.bemtivi.bemtivi.application.domain.order.OrderItem;
import com.bemtivi.bemtivi.application.domain.product.Product;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.order.OrderEntity;
import com.bemtivi.bemtivi.persistence.entities.order.OrderItemEntity;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {
    OrderEntity mapToEntity(Order order);

    @Mapping(target = "customer", source = "customer", qualifiedByName = "mapToCustomerDomain")
    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "mapToOrderItemDomain")
    Order mapToDomain(OrderEntity order);

    @Named("mapToOrderItemDomain")
    @Mapping(target = "product", source = "product", qualifiedByName = "mapToProductDomain")
    OrderItem mapToOrderItemDomain(OrderItemEntity orderItem);
    @Named("mapToProductDomain")
    @Mapping(target = "categories", ignore = true)
    Product mapToProductDomain(ProductEntity productEntity);

    @Named("mapToCustomerDomain")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Customer mapToCustomerDomain(CustomerEntity customerEntity);

    default PageResponse<Order> mapToPageResponseDomain(Page<OrderEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Order> orders = pageResponse.getContent().stream().map(this::mapToDomain).collect(Collectors.toSet());
        return PageResponse.<Order>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(orders)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }
}
