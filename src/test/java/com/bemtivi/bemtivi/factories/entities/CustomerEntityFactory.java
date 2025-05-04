package com.bemtivi.bemtivi.factories.entities;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.AddressEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.TelephoneEntity;
import com.bemtivi.bemtivi.factories.ContractFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Slf4j
public class CustomerEntityFactory implements ContractFactory<CustomerEntity> {
    private Integer numberOfCustomer = 0;

    @Override
    public CustomerEntity createObject(boolean isActive) {
        numberOfCustomer++;
        CustomerEntity customerEntity = null;
        final String index = String.format("%02d", numberOfCustomer);
        final int MAX_CUSTOMERS = 32;
        if (numberOfCustomer < MAX_CUSTOMERS) {
            customerEntity = CustomerEntity.builder()
                    .name("nome" + index)
                    .email("email@email" + index + ".com")
                    .password("senha" + index)
                    .address(createAddress(index))
                    .telephones(createTelephone(index))
                    .pathImage("/caminho/da/imagem" + index + ".png")
                    .birthDate(LocalDate.parse("2002-03-" + index))
                    .pets(new LinkedHashSet<>())
                    .activationStatus(createActivationStatus(isActive))
                    .build();

        } else log.warn("NUMBER OF CUSTOMERS CREATED HAS REACHED THE LIMIT");
        return customerEntity;
    }

    private TelephoneEntity createTelephone(String index) {
        return TelephoneEntity.builder()
                .phoneOne("119123400" + index)
                .phoneTwo(null)
                .build();
    }

    private AddressEntity createAddress(String index) {
        return AddressEntity.builder()
                .state("estado" + index)
                .city("cidade" + index)
                .neighborhood("bairro" + index)
                .street("rua" + index)
                .number(numberOfCustomer)
                .complement("complemento" + index)
                .postalCode("07272-0" + index)
                .build();
    }

    private ActivationStatusEntity createActivationStatus(boolean isActive) {
        return ActivationStatusEntity.builder()
                .isActive(isActive)
                .creationDate(Instant.now())
                .build();
    }
}
