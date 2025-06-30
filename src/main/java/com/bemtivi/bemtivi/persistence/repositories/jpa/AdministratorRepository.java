package com.bemtivi.bemtivi.persistence.repositories.jpa;

import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.persistence.entities.jpa.administrator.AdministratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<AdministratorEntity, String> {
    @Query(value = "SELECT new com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO( c.id, c.name, c.email, c.isEmailActive, c.role, c.password ) FROM AdministratorEntity c WHERE c.email = ?1")
    Optional<UserAuthDTO> findByUsername(String email);
    Optional<AdministratorEntity> findByEmail(String email);
    List<AdministratorEntity> findByActivationStatus_IsActive(Boolean active);
}
