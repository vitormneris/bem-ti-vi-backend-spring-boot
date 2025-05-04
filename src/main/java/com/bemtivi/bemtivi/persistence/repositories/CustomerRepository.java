package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    @Query(value = "SELECT * FROM tb_clientes WHERE esta_ativo = ?1 AND nome ILIKE CONCAT('%', ?2, '%')", nativeQuery = true)
    Page<CustomerEntity> findByPagination(Boolean isActive, Pageable pageable, String name);
    Optional<CustomerEntity> findByEmail(String email);
}
