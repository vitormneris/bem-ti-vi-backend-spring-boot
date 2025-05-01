package com.bemtivi.bemtivi.controllers.in.service.mappers;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.service.dto.ServiceDTO;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.service.Service;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceWebMapper {
    PageResponseDTO<ServiceDTO> mapToPageResponseDto(PageResponse<Service> pageResponse);
    ServiceDTO mapToDTO(Service service);
    Service mapToDomain(ServiceDTO serviceDTO);
}
