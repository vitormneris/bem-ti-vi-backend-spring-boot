package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    Page<CategoryEntity> findByActivationStatus_IsActiveAndNameContainingIgnoreCase(Boolean isActive, String name, Pageable pageable);

}
