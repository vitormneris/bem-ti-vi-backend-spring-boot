package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.petservice.PetService;
import com.bemtivi.bemtivi.persistence.entities.petservice.PetServiceEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PetServicePersistenceMapper {
    PetServiceEntity mapToEntity(PetService petService);
    PetService mapToDomain(PetServiceEntity petServiceEntity);

    default PageResponse<PetService> mapToPageResponseDomain(Page<PetServiceEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<PetService> petServices = pageResponse.getContent().stream().map(this::mapToDomain).collect(Collectors.toSet());
        return PageResponse.<PetService>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(petServices)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }

}
