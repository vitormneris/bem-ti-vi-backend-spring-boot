package com.bemtivi.bemtivi.controllers.in.order.mappers;

import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.OrderDTO;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.order.Order;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrderWebMapper {
    OrderDTO mapToDTO(Order order);
    Order mapToDomain(OrderDTO orderDTO);

    PageResponseDTO<OrderDTO> mapToPageResponseDto(PageResponse<Order> pageResponse);
}
