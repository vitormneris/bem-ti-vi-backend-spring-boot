package com.bemtivi.bemtivi.controllers.in.petservice.mappers;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.petservice.dto.PetServiceDTO;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.petservice.PetService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetServiceWebMapper {
    PageResponseDTO<PetServiceDTO> mapToPageResponseDto(PageResponse<PetService> pageResponse);
    PetServiceDTO mapToDTO(PetService petService);
    PetService mapToDomain(PetServiceDTO petServiceDTO);
}
