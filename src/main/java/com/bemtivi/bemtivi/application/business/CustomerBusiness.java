package com.bemtivi.bemtivi.application.business;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.customer.Address;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.user.customer.Telephone;
import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.DuplicateResourceException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.customer.AddressEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.TelephoneEntity;
import com.bemtivi.bemtivi.persistence.mappers.CustomerPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomerBusiness {
    private final CustomerRepository customerRepository;
    private final CustomerPersistenceMapper mapper;
    private final UploadBusiness uploadManager;

    public PageResponse<Customer> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                customerRepository.findByActivationStatus_IsActiveAndNameContainingIgnoreCase(isActive, name == null ? "" : name, PageRequest.of(page, pageSize))
        );
    }

    public Customer findById(String id) {
        return mapper.mapToDomain(customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        ));
    }

    public Customer insert(Customer customer, MultipartFile file) {
        customerRepository.findByUsername(customer.getEmail()).ifPresent((register) -> {
            throw new DuplicateResourceException(RuntimeErrorEnum.ERR0013);
        });

        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        customer.setId(null);
        customer.setActivationStatus(activationStatus);
        customer.setPathImage(uploadManager.uploadObject(file));
        customer.setRole(UserRoleEnum.CUSTOMER);
        customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        CustomerEntity saved;
        try {
            saved = customerRepository.save(mapper.mapToEntity(customer));
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Customer update(String id, Customer customerNew, MultipartFile file) {
        CustomerEntity customerOld = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );
        customerOld.setName(customerNew.getName() == null ? customerOld.getName() : customerNew.getName());
        customerOld.setBirthDate(customerNew.getBirthDate() == null ? customerOld.getBirthDate() : customerNew.getBirthDate());

        if (customerNew.getTelephones() != null) {
            Telephone telephoneNew = customerNew.getTelephones();
            TelephoneEntity telephoneOld = customerOld.getTelephones();
            customerOld.setTelephones(TelephoneEntity.builder()
                    .id(telephoneOld.getId())
                    .phoneOne(telephoneNew.getPhoneOne() == null ? telephoneOld.getPhoneOne() : telephoneNew.getPhoneOne())
                    .phoneTwo(telephoneNew.getPhoneTwo() == null ? telephoneOld.getPhoneTwo() : telephoneNew.getPhoneTwo())
                    .build());
        }

        if (customerNew.getAddress() != null) {
            Address addressNew = customerNew.getAddress();
            AddressEntity addressOld = customerOld.getAddress();
            customerOld.setAddress(AddressEntity.builder()
                            .id(addressOld.getId())
                            .state(addressNew.getState() == null ? addressOld.getState() : addressNew.getState())
                            .city(addressNew.getCity() == null ? addressOld.getCity() : addressNew.getCity())
                            .neighborhood(addressNew.getNeighborhood() == null ? addressOld.getNeighborhood() : addressNew.getNeighborhood())
                            .street(addressNew.getStreet() == null ? addressOld.getStreet() : addressNew.getStreet())
                            .number(addressNew.getNumber() == null ? addressOld.getNumber() : addressNew.getNumber())
                            .complement(addressNew.getComplement() == null ? addressOld.getComplement() : addressNew.getComplement())
                            .postalCode(addressNew.getPostalCode() == null ? addressOld.getPostalCode() : addressNew.getPostalCode())
                            .build());
        }
        if (file != null) customerOld.setPathImage(uploadManager.uploadObject(file));
        CustomerEntity updated;
        try {
            updated = customerRepository.save(customerOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        CustomerEntity service = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );
        service.getActivationStatus().setIsActive(false);
        service.getActivationStatus().setDeactivationDate(Instant.now());
        customerRepository.save(service);
    }

    public void delete(String id) {
        CustomerEntity service = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );
        customerRepository.delete(service);
    }
}
