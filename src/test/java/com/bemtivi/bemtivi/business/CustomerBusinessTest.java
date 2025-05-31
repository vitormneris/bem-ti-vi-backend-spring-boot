package com.bemtivi.bemtivi.business;

import com.bemtivi.bemtivi.application.business.user.CustomerBusiness;
import com.bemtivi.bemtivi.application.business.UploadBusiness;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.exceptions.DuplicateResourceException;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.mappers.CustomerPersistenceMapper;
import com.bemtivi.bemtivi.factories.domains.CustomerFactory;
import com.bemtivi.bemtivi.factories.ContractFactory;
import com.bemtivi.bemtivi.factories.ImageFactory;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import com.bemtivi.bemtivi.factories.entities.CustomerEntityFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerBusinessTest {

    @Autowired
    private CustomerBusiness customerBusiness;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private CustomerPersistenceMapper mapper;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private UploadBusiness uploadManager;

    private final ContractFactory<Customer> factoryDomain = new CustomerFactory();
    private final ContractFactory<CustomerEntity> factoryEntity = new CustomerEntityFactory();
    private final ImageFactory imageFactory = new ImageFactory();

    @BeforeEach
    void setup() {
        List<CustomerEntity> customers = factoryEntity.createMultipleObjects(4);
        customers.forEach(entityManager::persist);
    }

    @Test
    @DisplayName("Should save a Customer in the database")
    void insertCase1() {
        MultipartFile file = imageFactory.createMultipartFile();
        Customer newCustomer = factoryDomain.createOneObject();
        when(uploadManager.uploadObject(file)).thenReturn("/path/to/image.png");
        Customer savedCustomer = customerBusiness.insert(newCustomer, file);
        verify(uploadManager, times(1)).uploadObject(file);
        assertThat(savedCustomer)
                .usingRecursiveComparison()
                .ignoringFields("id", "telephones.id", "address.id")
                .isEqualTo(newCustomer);
    }

    @Test
    @DisplayName("Should throw an Exception when try insert that already exist email in the database")
    void insertCase2() {
        MultipartFile file = imageFactory.createMultipartFile();
        when(uploadManager.uploadObject(file)).thenReturn("/path/to/image.png");
        Customer newCustomer = factoryDomain.createOneObject();
        newCustomer.setEmail("email@email01.com");
        verify(uploadManager, times(0)).uploadObject(file);
        Assertions.assertThrows(DuplicateResourceException.class, () ->
            customerBusiness.insert(newCustomer, file)
        );
    }
}