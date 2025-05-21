package com.bemtivi.bemtivi.persistence.mappers;


import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.pet.Pet;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.pet.PetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PetPersistenceMapper {
    PetEntity mapToEntity(Pet pet);

    @Mapping(target = "owner", source = "owner", qualifiedByName = "mapEntityToOwnerDomain")
    Pet mapToDomain(PetEntity pet);
    @Named(value = "mapEntityToOwnerDomain")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Customer mapEntityToPetDomain(CustomerEntity customer);

    default PageResponse<Pet> mapToPageResponseDomain(Page<PetEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Pet> pets = pageResponse.getContent().stream().map(this::mapToDomain).collect(Collectors.toSet());
        return PageResponse.<Pet>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(pets)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }
}
