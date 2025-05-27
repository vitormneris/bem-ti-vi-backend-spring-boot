package com.bemtivi.bemtivi.controllers.in.customer.mappers;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CustomerWebMapper {
    PageResponseDTO<CustomerDTO> mapToPageResponseDto(PageResponse<Customer> pageResponse);
    Customer mapToDomain(CustomerDTO costumer);
    CustomerDTO mapToDTO(Customer customer);
}
