package com.bemtivi.bemtivi.application.business;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.DuplicateResourceException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.administrator.AdministratorEntity;
import com.bemtivi.bemtivi.persistence.mappers.AdministratorPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AdministratorBusiness {
    private final AdministratorRepository administratorRepository;
    private final AdministratorPersistenceMapper mapper;

    public PageResponse<Administrator> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                administratorRepository.findByActivationStatus_IsActiveAndNameContainingIgnoreCase(isActive, name == null ? "" : name, PageRequest.of(page, pageSize))
        );
    }

    public Administrator findById(String id) {
        return mapper.mapToDomain(administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0010)
        ));
    }

    public Administrator insert(Administrator administrator) {
        administratorRepository.findByEmail(administrator.getEmail()).ifPresent((register) -> {
            throw new DuplicateResourceException(RuntimeErrorEnum.ERR0013);
        });
        AdministratorEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            administrator.setId(null);
            administrator.setActivationStatus(activationStatus);
            AdministratorEntity administratorEntity = mapper.mapToEntity(administrator);
            saved = administratorRepository.save(administratorEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException((RuntimeErrorEnum.ERR0002));
        }
        return mapper.mapToDomain(saved);
    }

    public Administrator update(String id, Administrator administratorNew) {
        AdministratorEntity administratorOld = administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0010)
        );
        administratorOld.setName(administratorNew.getName() == null ? administratorOld.getName() : administratorNew.getName());
        AdministratorEntity updated;
        try {
            updated = administratorRepository.save(administratorOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        AdministratorEntity service = administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0010)
        );
        service.getActivationStatus().setIsActive(false);
        service.getActivationStatus().setDeactivationDate(Instant.now());
        administratorRepository.save(service);
    }

    public void delete(String id) {
        AdministratorEntity service = administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0010)
        );
        administratorRepository.delete(service);
    }
}
