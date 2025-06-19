package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.order.Order;
import com.bemtivi.bemtivi.application.domain.order.OrderItem;
import com.bemtivi.bemtivi.application.domain.pet.Pet;
import com.bemtivi.bemtivi.application.domain.product.Product;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.persistence.entities.appointment.AppointmentEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.order.OrderEntity;
import com.bemtivi.bemtivi.persistence.entities.order.OrderItemEntity;
import com.bemtivi.bemtivi.persistence.entities.pet.PetEntity;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CustomerPersistenceMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "activationStatus", target = "activationStatus")
    CustomerEntity mapToEntity(Customer customer);

    @Mapping(target = "pets", source = "pets", qualifiedByName = "mapToPetsDomain")
    @Mapping(target = "orders", source = "orders", qualifiedByName = "mapToOrdersDomain")
    @Mapping(target = "appointments", source = "appointments", qualifiedByName = "mapToServicesDomain")
    @Mapping(source = "role", target = "role", ignore = true)
    @Mapping(source = "password", target = "password", ignore = true)
    Customer mapToDomain(CustomerEntity customerEntity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "password", target = "password")
    Customer mapUserAuthDTOToDomain(UserAuthDTO userAuthDTO);

    @Named(value = "mapToPetsDomain")
    default Set<Pet> mapToPetsDomain(Set<PetEntity> petEntities) {
        if (petEntities == null) return new HashSet<>();
        return petEntities.stream().map(this::mapToPetDomain).collect(Collectors.toSet());
    }

    @Named(value = "mapToOrdersDomain")
    default Set<Order> mapToOrdersDomain(Set<OrderEntity> orderEntities) {
        if (orderEntities == null) return new HashSet<>();
        return orderEntities.stream().map(this::mapToOrderDomain).collect(Collectors.toSet());
    }

    @Named(value = "mapToServicesDomain")
    default Set<Appointment> mapToServicesDomain(Set<AppointmentEntity> appointmentEntities) {
        if (appointmentEntities == null) return new HashSet<>();
        return appointmentEntities.stream().map(this::mapToAppointmentDomain).collect(Collectors.toSet());
    }

    @Mapping(target = "owner", ignore = true)
    Pet mapToPetDomain(PetEntity petEntity);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "mapToOrderItemDomain")
    Order mapToOrderDomain(OrderEntity order);

    @Named("mapToOrderItemDomain")
    @Mapping(target = "product", source = "product", qualifiedByName = "mapToProductDomain")
    OrderItem mapToOrderItemDomain(OrderItemEntity orderItem);
    @Named("mapToProductDomain")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Product mapToProductDomain(ProductEntity productEntity);

    @Mapping(target = "customer", ignore = true)
    Appointment mapToAppointmentDomain(AppointmentEntity  appointmentEntity);

    default PageResponse<Customer> mapToPageResponseDomain(Page<CustomerEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Customer> customers = new LinkedHashSet<>(pageResponse.getContent().stream().map(this::mapToDomain).toList());
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
