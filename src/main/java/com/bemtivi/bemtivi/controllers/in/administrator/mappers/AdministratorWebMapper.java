package com.bemtivi.bemtivi.controllers.in.administrator.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.AdministratorDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AdministratorWebMapper {
    PageResponseDTO<AdministratorDTO> mapToPageResponseDto(PageResponse<Administrator> pageResponse);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "activationStatus", target = "activationStatus")
    Administrator mapToDomain(AdministratorDTO administratorDTO);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role", ignore = true)
    @Mapping(source = "password", target = "password", ignore = true)
    @Mapping(source = "activationStatus", target = "activationStatus")
    AdministratorDTO mapToDTO(Administrator administrator);

    Set<AdministratorDTO> mapToSetDTO(Set<Administrator> administrators);
}
