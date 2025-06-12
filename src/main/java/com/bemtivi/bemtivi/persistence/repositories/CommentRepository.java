package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.comment.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    Page<CommentEntity> findByActivationStatus_IsActiveAndService_Id(Boolean isActive, String serviceId, Pageable pageable);
    Page<CommentEntity> findByActivationStatus_IsActiveAndProduct_Id(Boolean isActive, String productId, Pageable pageable);

}
