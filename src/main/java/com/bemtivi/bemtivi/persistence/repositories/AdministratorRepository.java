package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.administrator.AdministratorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<AdministratorEntity, String> {
    Page<AdministratorEntity> findByActivationStatus_IsActiveAndNameContainingIgnoreCase(Boolean isActive, String name, Pageable pageable);
    Optional<AdministratorEntity> findByEmail(String email);
}
