package com.bemtivi.bemtivi.factories.domains;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.user.customer.Address;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.user.customer.Telephone;
import com.bemtivi.bemtivi.factories.ContractFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Slf4j
public class CustomerFactory implements ContractFactory<Customer> {
    private Integer numberOfCustomer = 0;

    @Override
    public Customer createObject(boolean isActive) {
        numberOfCustomer++;
        Customer customer = null;
        final String index = String.format("%02d", numberOfCustomer);
        final int MAX_CUSTOMERS = 32;
        if (numberOfCustomer < MAX_CUSTOMERS) {
            customer = Customer.builder()
                    .name("nome" + index)
                    .email("email@email" + index)
                    .password("senha" + index)
                    .address(createAddress(index))
                    .telephones(createTelephone(index))
                    .pathImage("/path/to/image.png")
                    .birthDate(LocalDate.parse("2002-03-" + index))
                    .pets(new LinkedHashSet<>())
                    .activationStatus(createActivationStatus(isActive))
                    .build();

        } else log.warn("NUMBER OF CUSTOMERS CREATED HAS REACHED THE LIMIT");
        return customer;
    }

    private Telephone createTelephone(String index) {
        return Telephone.builder()
                .phoneOne("119123400" + index)
                .phoneTwo(null)
                .build();
    }

    private Address createAddress(String index) {
        return Address.builder()
                .state("estado" + index)
                .city("cidade" + index)
                .neighborhood("bairro" + index)
                .street("rua" + index)
                .number(numberOfCustomer)
                .complement("complemento" + index)
                .postalCode("07272-0" + index)
                .build();
    }

    private ActivationStatus createActivationStatus(boolean isActive) {
        return ActivationStatus.builder()
                .isActive(isActive)
                .creationDate(Instant.now())
                .build();
    }
}
