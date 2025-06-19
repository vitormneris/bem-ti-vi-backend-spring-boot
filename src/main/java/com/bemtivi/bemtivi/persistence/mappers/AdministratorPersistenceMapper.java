package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.persistence.entities.administrator.AdministratorEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AdministratorPersistenceMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "activationStatus", target = "activationStatus")
    AdministratorEntity mapToEntity(Administrator Administrator);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role", ignore = true)
    @Mapping(source = "password", target = "password", ignore = true)
    @Mapping(source = "activationStatus", target = "activationStatus")
    Administrator mapToDomain(AdministratorEntity AdministratorEntity);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "password", target = "password")
    Administrator mapUserAuthDTOToDomain(UserAuthDTO userAuthDTO);
    Set<Administrator> mapToSetDomain(Set<AdministratorEntity> administrators);
}
