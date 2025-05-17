package com.bemtivi.bemtivi.controllers.in.order.mappers;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.OrderDTO;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.order.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderWebMapper {
    OrderDTO mapToDTO(Order order);
    Order mapToDomain(OrderDTO orderDTO);
    PageResponseDTO<OrderDTO> mapToPageResponseDto(PageResponse<Order> pageResponse);
}
