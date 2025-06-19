package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.appointment.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AppointmentPersistenceMapper {
    AppointmentEntity mapToEntity(Appointment appointment);

    @Mapping(target = "customer", source = "customer", qualifiedByName = "mapToCustomerDomain")
    Appointment mapToDomain(AppointmentEntity appointment);

    @Named("mapToCustomerDomain")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(source = "role", target = "role", ignore = true)
    @Mapping(source = "password", target = "password", ignore = true)
    Customer mapToCustomerDomain(CustomerEntity customerEntity);

    default PageResponse<Appointment> mapToPageResponseDomain(Page<AppointmentEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Appointment> appointments = new LinkedHashSet<>(pageResponse.getContent().stream().map(this::mapToDomain).toList());
        return PageResponse.<Appointment>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(appointments)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }
}
