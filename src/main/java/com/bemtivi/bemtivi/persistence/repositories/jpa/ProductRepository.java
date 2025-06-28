package com.bemtivi.bemtivi.persistence.repositories.jpa;

import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    Page<ProductEntity> findByActivationStatus_IsActiveAndNameContainingIgnoreCase(Boolean isActive, String name, Pageable pageable);
}
