package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.customer.Customer;
import com.bemtivi.bemtivi.domain.pet.Pet;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.pet.PetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {
    CustomerEntity mapToEntity(Customer customer);

    @Mapping(target = "pets", source = "pets", qualifiedByName = "mapToPetsDomain")
    Customer mapToDomain(CustomerEntity customer);

    @Named(value = "mapToPetsDomain")
    default Set<Pet> mapToPetsDomain(Set<PetEntity> petEntities) {
            if (petEntities == null) return new HashSet<>();
            return petEntities.stream().map(this::mapToPetDomain).collect(Collectors.toSet());
    }

    @Mapping(target = "owner", ignore = true)
    Pet mapToPetDomain(PetEntity petEntity);

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
