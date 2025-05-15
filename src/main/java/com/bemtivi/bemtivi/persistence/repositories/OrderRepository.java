package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.order.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    @Query(value = "SELECT * FROM tb_pedidos WHERE esta_ativo = ?1 AND momento ILIKE CONCAT('%', ?2, '%')", nativeQuery = true)
    Page<OrderEntity> findByPagination(Boolean isActive, Pageable pageable, String query);
}
