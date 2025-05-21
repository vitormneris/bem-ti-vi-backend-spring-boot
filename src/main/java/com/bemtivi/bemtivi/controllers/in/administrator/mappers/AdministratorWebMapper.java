package com.bemtivi.bemtivi.controllers.in.administrator.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.AdministratorDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AdministratorWebMapper {
    PageResponseDTO<AdministratorDTO> mapToPageResponseDto(PageResponse<Administrator> pageResponse);
    Administrator mapToDomain(AdministratorDTO administratorDTO);
    AdministratorDTO mapToDTO(Administrator administrator);
}
