package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.pet.PetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<PetEntity, String> {
    Page<PetEntity> findByActivationStatus_IsActiveAndNameContainingIgnoreCase(Boolean isActive, String name, Pageable pageable);
}
