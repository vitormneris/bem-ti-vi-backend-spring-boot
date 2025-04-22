package com.bemtivi.bemtivi.services;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.petservice.PetService;
import com.bemtivi.bemtivi.exceptions.DataIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.petservice.PetServiceEntity;
import com.bemtivi.bemtivi.persistence.mappers.PetServicePersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.PetServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PetServiceService {
    private final PetServiceRepository petServiceRepository;
    private final PetServicePersistenceMapper mapper;

    public PageResponse<PetService> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                petServiceRepository.findByPagination(isActive, PageRequest.of(page, pageSize), name == null ? "" : name)
        );
    }

    public PetService findById(String id) {
        return mapper.mapToDomain(petServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        ));
    }

    public PetService insert(PetService petService) {
        PetServiceEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            petService.setId(null);
            petService.setActivationStatus(activationStatus);
            PetServiceEntity petServiceEntity = mapper.mapToEntity(petService);
            saved = petServiceRepository.save(petServiceEntity);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public PetService update(String id, PetService petServiceNew) {
        PetServiceEntity serviceOld = petServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        );
        serviceOld.setName(petServiceNew.getName() == null ? serviceOld.getName() : petServiceNew.getName());
        serviceOld.setPrice(petServiceNew.getPrice() == null ? serviceOld.getPrice() : petServiceNew.getPrice());
        serviceOld.setEstimated_duration(petServiceNew.getEstimated_duration() == null ? serviceOld.getEstimated_duration() : petServiceNew.getEstimated_duration());
        serviceOld.setDescription(petServiceNew.getDescription() == null ? serviceOld.getDescription() : petServiceNew.getDescription());
        serviceOld.setPathImage(petServiceNew.getPathImage() == null ? serviceOld.getPathImage() : petServiceNew.getPathImage());
        PetServiceEntity updated;
        try {
            updated = petServiceRepository.save(serviceOld);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        PetServiceEntity service = petServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        );
        service.getActivationStatus().setIsActive(false);
        service.getActivationStatus().setDeactivationDate(Instant.now());
        petServiceRepository.save(service);
    }

    public void delete(String id) {
        PetServiceEntity service = petServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        );
        petServiceRepository.delete(service);
    }
}
