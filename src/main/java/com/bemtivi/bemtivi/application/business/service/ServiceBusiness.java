package com.bemtivi.bemtivi.application.business.service;

import com.bemtivi.bemtivi.application.business.UploadBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.service.Service;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.jpa.service.ServiceEntity;
import com.bemtivi.bemtivi.persistence.mappers.ServicePersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.jpa.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceBusiness {
    private final ServiceRepository serviceRepository;
    private final ServicePersistenceMapper mapper;
    private final UploadBusiness uploadManager;

    public PageResponse<Service> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                serviceRepository.findByActivationStatus_IsActiveAndNameContainingIgnoreCase(isActive, name == null ? "" : name, PageRequest.of(page, pageSize))
        );
    }

    public Service findById(String id) {
        return mapper.mapToDomain(serviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        ));
    }

    public Service insert(Service service, MultipartFile file) {
        ServiceEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            service.setId(null);
            service.setRate(0d);
            service.setActivationStatus(activationStatus);
            ServiceEntity serviceEntity = mapper.mapToEntity(service);
            serviceEntity.setPathImage(uploadManager.uploadObject(file));
            saved = serviceRepository.save(serviceEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Service update(String id, Service serviceNew, MultipartFile file) {
        ServiceEntity petServiceOld = serviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        );
        petServiceOld.setName(serviceNew.getName() == null ? petServiceOld.getName() : serviceNew.getName());
        petServiceOld.setPrice(serviceNew.getPrice() == null ? petServiceOld.getPrice() : serviceNew.getPrice());
        petServiceOld.setEstimatedDuration(serviceNew.getEstimatedDuration() == null ? petServiceOld.getEstimatedDuration() : serviceNew.getEstimatedDuration());
        petServiceOld.setDescription(serviceNew.getDescription() == null ? petServiceOld.getDescription() : serviceNew.getDescription());
        if (file != null) petServiceOld.setPathImage(uploadManager.uploadObject(file));
        ServiceEntity updated;
        try {
            updated = serviceRepository.save(petServiceOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        ServiceEntity service = serviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        );
        service.getActivationStatus().setIsActive(false);
        service.getActivationStatus().setDeactivationDate(Instant.now());
        serviceRepository.save(service);
    }

    public void delete(String id) {
        ServiceEntity service = serviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        );
        serviceRepository.delete(service);
    }
}
