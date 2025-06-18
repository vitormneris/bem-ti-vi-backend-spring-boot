package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.persistence.entities.administrator.AdministratorEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    Page<CustomerEntity> findByActivationStatus_IsActiveAndNameContainingIgnoreCase(Boolean isActive, String name, Pageable pageable);
    @Query(value = "SELECT new com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO( c.id, c.name, c.email, c.isEmailActive, c.role, c.password ) FROM CustomerEntity c WHERE c.email = ?1")
    Optional<UserAuthDTO> findByUsername(String email);
    Optional<CustomerEntity> findByEmail(String email);
}
