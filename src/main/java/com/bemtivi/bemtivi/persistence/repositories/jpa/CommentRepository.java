package com.bemtivi.bemtivi.persistence.repositories.jpa;

import com.bemtivi.bemtivi.persistence.entities.jpa.comment.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    Page<CommentEntity> findByActivationStatus_IsActiveAndService_Id(Boolean isActive, String serviceId, Pageable pageable);
    Page<CommentEntity> findByActivationStatus_IsActiveAndProduct_Id(Boolean isActive, String productId, Pageable pageable);
    List<CommentEntity> findByActivationStatus_IsActiveAndProduct_Id(Boolean isActive, String productId);
    List<CommentEntity> findByActivationStatus_IsActiveAndService_Id(Boolean isActive, String serviceId);
}
