package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.factories.entities.CustomerEntityFactory;
import com.bemtivi.bemtivi.factories.ContractFactory;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EntityManager entityManager;

    ContractFactory<CustomerEntity> entityFactory = new CustomerEntityFactory();

    @BeforeEach
    void setup() {
        List<CustomerEntity> customers = entityFactory.createMultipleObjects(8);
        customers.forEach(entityManager::persist);
        List<CustomerEntity> customersDisable = entityFactory.createMultipleDeactivationObjects(6);
        customersDisable.forEach(entityManager::persist);
    }

    @Test
    @DisplayName("Should insert a customer into the database successfully")
    void save() {
        CustomerEntity newCustomer = entityFactory.createOneObject();
        CustomerEntity savedCustomer =  customerRepository.save(newCustomer);
        assertThat(savedCustomer.getEmail()).isEqualTo(newCustomer.getEmail());
    }

    @Test
    @DisplayName("Should successfully get a Customer from database")
    void findByEmailCase1() {
        Optional<CustomerEntity> object = customerRepository.findByEmail("email@email01.com");
        assertThat(object.isPresent()).isTrue();
        assertThat(object.get().getEmail()).isEqualTo("email@email01.com");
    }

    @Test
    @DisplayName("Should not get a Customer from database when Customer not exists")
    void findByEmailCase2() {
        Optional<CustomerEntity> object = customerRepository.findByEmail("email@email00");
        assertThat(object.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should successfully get five Customers from database")
    void findByPaginationCase1() {
        Page<CustomerEntity> pages =  customerRepository.findByPagination(true, PageRequest.of(0, 10), "nome");
        assertThat(pages.getContent().size()).isEqualTo(8);
    }

    @Test
    @DisplayName("Should successfully get three Customers from database because the page size is three")
    void findByPaginationCase2() {
        Page<CustomerEntity> pages =  customerRepository.findByPagination(true, PageRequest.of(0, 3), "nome");
        assertThat(pages.getContent().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should not successfully get Customers from database because the name is wrong")
    void findByPaginationCase3() {
        Page<CustomerEntity> pages =  customerRepository.findByPagination(true, PageRequest.of(0, 10), "car");
        assertThat(pages.getContent().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should successfully get six disable Customers from database")
    void findByPaginationCase4() {
        Page<CustomerEntity> pages =  customerRepository.findByPagination(false, PageRequest.of(0, 10), "nome");
        assertThat(pages.getContent().size()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should successfully get three Customers with limit of 5 elements from second page")
    void findByPaginationCase5() {
        Page<CustomerEntity> pages =  customerRepository.findByPagination(true, PageRequest.of(1, 5), "nome");
        assertThat(pages.getContent().size()).isEqualTo(3);
    }
}