package com.bemtivi.bemtivi.application.business.user;

import com.bemtivi.bemtivi.application.business.UploadBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.OperationNotAllowed;
import org.springframework.dao.DataIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.DuplicateResourceException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.administrator.AdministratorEntity;
import com.bemtivi.bemtivi.persistence.mappers.AdministratorPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdministratorBusiness {
    private final AdministratorRepository administratorRepository;
    private final AdministratorPersistenceMapper mapper;
    private final UploadBusiness uploadBusiness;

    public Set<Administrator> findAll() {
        return mapper.mapToSetDomain(new LinkedHashSet<>(administratorRepository.findAll()));
    }

    public Administrator findById(String id) {
        return mapper.mapToDomain(administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0010)
        ));
    }

    public Administrator insert(Administrator administrator, MultipartFile file) {
        administratorRepository.findByUsername(administrator.getEmail()).ifPresent((register) -> {
            throw new DuplicateResourceException(RuntimeErrorEnum.ERR0013);
        });

        if (administratorRepository.findAll().size() >= 6) {
            throw new OperationNotAllowed(RuntimeErrorEnum.ERR0017);
        }

        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        administrator.setId(null);
        administrator.setRole(UserRoleEnum.ADMINISTRATOR);
        administrator.setPassword(new BCryptPasswordEncoder().encode(administrator.getPassword()));
        administrator.setActivationStatus(activationStatus);
        administrator.setPathImage(uploadBusiness.uploadObject(file));

        AdministratorEntity saved;
        try {
            saved = administratorRepository.save(mapper.mapToEntity(administrator));
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException((RuntimeErrorEnum.ERR0002));
        }
        return mapper.mapToDomain(saved);
    }

    public Administrator update(String id, Administrator administratorNew, MultipartFile file) {
        AdministratorEntity administratorOld = administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0010)
        );
        administratorOld.setName(administratorNew.getName() == null ? administratorOld.getName() : administratorNew.getName());
        if (file != null) administratorOld.setPathImage(uploadBusiness.uploadObject(file));
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
