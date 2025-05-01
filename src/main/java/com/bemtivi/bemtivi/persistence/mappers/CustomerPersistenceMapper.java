package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.customer.Customer;
import com.bemtivi.bemtivi.domain.customer.Telephone;
import com.bemtivi.bemtivi.domain.pet.Pet;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.TelephoneEntity;
import com.bemtivi.bemtivi.persistence.entities.pet.PetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {
    CustomerEntity mapToEntity(Customer customer);
    @Mapping(target = "telephones", source = "telephones", qualifiedByName = "mapToTelephoneDomain")
    @Mapping(target = "pets", source = "pets", qualifiedByName = "mapToPetsDomain")
    Customer mapToDomain(CustomerEntity customer);

    @Named(value = "mapToTelephoneDomain")
    default Telephone mapToTelephoneDomain(TelephoneEntity telephoneEntity) {
        return Telephone.builder()
                .id(telephoneEntity.getId())
                .phoneOne(telephoneEntity.getPhoneOne())
                .phoneTwo(telephoneEntity.getPhoneTwo())
                .build();
    }

    @Named(value = "mapToPetsDomain")
    default Set<Pet> mapToPetsDomain(Set<PetEntity> petEntities) {
        Set<Pet> pets = new LinkedHashSet<>();
        if (petEntities != null) {
            petEntities.forEach((petEntity) ->
                    pets.add(
                            Pet.builder()
                                    .id(petEntity.getId())
                                    .name(petEntity.getName())
                                    .race(petEntity.getRace())
                                    .size(petEntity.getSize())
                                    .gender(petEntity.getGender())
                                    .birthDate(petEntity.getBirthDate())
                                    .species(petEntity.getSpecies())
                                    .note(petEntity.getNote())
                                    .pathImage(petEntity.getPathImage())
                                    .activationStatus(mapEntityToActivationStatusDomain(petEntity.getActivationStatus()))
                                    .build()
                    )
            );
        }
        return pets;
    }

    ActivationStatus mapEntityToActivationStatusDomain(ActivationStatusEntity activationStatus);

    default PageResponse<Customer> mapToPageResponseDomain(Page<CustomerEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Customer> customers = pageResponse.getContent().stream().map(this::mapToDomain).collect(Collectors.toSet());
        return PageResponse.<Customer>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(customers)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }
}
