package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    @Query(value = "SELECT * FROM tb_produtos WHERE esta_ativo = ?1 AND nome ILIKE CONCAT('%', ?2, '%')", nativeQuery = true)
    Page<ProductEntity> findByPagination(Boolean isActive, Pageable pageable, String name);
}
