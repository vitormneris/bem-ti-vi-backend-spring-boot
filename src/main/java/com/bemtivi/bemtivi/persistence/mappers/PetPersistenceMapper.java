package com.bemtivi.bemtivi.persistence.mappers;

import com.amazonaws.services.wellarchitected.model.AdditionalResources;
import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.customer.Address;
import com.bemtivi.bemtivi.domain.customer.Customer;
import com.bemtivi.bemtivi.domain.customer.Telephone;
import com.bemtivi.bemtivi.domain.pet.Pet;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.AddressEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.TelephoneEntity;
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
    default Customer mapEntityToPetDomain(CustomerEntity customer) {
        return Customer.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .birthDate(customer.getBirthDate())
                .address(mapEntityToAddressDomain(customer.getAddress()))
                .telephones(mapEntityToTelephoneDomain(customer.getTelephones()))
                .activationStatus(mapEntityToActivationStatusDomain(customer.getActivationStatus()))
                .build();
    }

    Address mapEntityToAddressDomain(AddressEntity address);
    Telephone mapEntityToTelephoneDomain(TelephoneEntity telephone);
    ActivationStatus mapEntityToActivationStatusDomain(ActivationStatusEntity activationStatus);

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
