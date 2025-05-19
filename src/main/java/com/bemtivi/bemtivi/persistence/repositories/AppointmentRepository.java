package com.bemtivi.bemtivi.persistence.repositories;

import com.bemtivi.bemtivi.persistence.entities.appointment.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, String> {
    @Query(value = "SELECT * FROM tb_agendamentos WHERE esta_ativo = ?1 AND momento BETWEEN ?2 AND ?3", nativeQuery = true)
    Page<AppointmentEntity> findByPagination(Boolean isActive, Pageable pageable, LocalDate momentStart, LocalDate momentEnd);
}
