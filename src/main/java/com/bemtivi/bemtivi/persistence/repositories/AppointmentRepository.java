package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.appointment.AppointmentEntity;
import com.bemtivi.bemtivi.persistence.entities.order.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, String> {
    @Query(value = "SELECT * FROM tb_agendamentos WHERE esta_ativo = ?1 AND data_e_hora BETWEEN ?2 AND ?3", nativeQuery = true)
    Page<AppointmentEntity> findByPagination(Boolean isActive, Pageable pageable, LocalDate momentStart, LocalDate momentEnd);

    @Query("""
    SELECT o FROM AppointmentEntity o
    WHERE o.activationStatus.isActive = :isActive
    AND o.customer.id = :customerId
    AND (:paymentStatus IS NULL OR LOWER(o.paymentStatus) LIKE LOWER(CONCAT('%', :paymentStatus, '%')))
""")
    Page<AppointmentEntity> findAppointments(
            @Param("isActive") Boolean isActive,
            @Param("customerId") String customerId,
            @Param("paymentStatus") String paymentStatus,
            Pageable pageable
    );
}
