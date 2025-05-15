package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.order.Order;
import com.bemtivi.bemtivi.persistence.entities.order.OrderEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {
    OrderEntity mapToEntity(Order order);
    Order mapToDomain(OrderEntity order);

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
