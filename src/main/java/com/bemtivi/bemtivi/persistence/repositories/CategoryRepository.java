package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    @Query(value = "SELECT * FROM tb_categorias WHERE esta_ativo = ?1 AND nome ILIKE CONCAT('%', ?2, '%')", nativeQuery = true)
    Page<CategoryEntity> findByPagination(Boolean isActive, Pageable pageable, String name);
}
