package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    Page<CustomerEntity> findByActivationStatus_IsActiveAndNameContainingIgnoreCase(Boolean isActive, String name, Pageable pageable);
    Optional<CustomerEntity> findByEmail(String email);
}
