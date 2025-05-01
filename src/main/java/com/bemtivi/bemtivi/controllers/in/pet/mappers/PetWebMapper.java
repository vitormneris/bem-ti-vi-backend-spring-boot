package com.bemtivi.bemtivi.controllers.in.pet.mappers;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.pet.dto.PetDTO;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.pet.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetWebMapper {
    PageResponseDTO<PetDTO> mapToPageResponseDto(PageResponse<Pet> pageResponse);
    Pet mapToDomain(PetDTO costumer);
    PetDTO mapToDTO(Pet costumer);
}
