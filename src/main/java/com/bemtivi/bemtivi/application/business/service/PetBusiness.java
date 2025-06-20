package com.bemtivi.bemtivi.application.business.service;

import com.bemtivi.bemtivi.application.business.UploadBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.application.domain.pet.Pet;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.pet.PetEntity;
import com.bemtivi.bemtivi.persistence.mappers.PetPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import com.bemtivi.bemtivi.persistence.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PetBusiness {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;
    private final PetPersistenceMapper mapper;
    private final UploadBusiness uploadManager;

    public PageResponse<Pet> findByPagination(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                petRepository.findByActivationStatus_IsActiveAndNameContainingIgnoreCase(isActive, name == null ? "" : name, PageRequest.of(page, pageSize))
        );
    }

    public PageResponse<Pet> findByActivationStatus_IsActiveAndOwner_Id(Boolean isActive, String customerId, Integer pageSize, Integer page) {
        return mapper.mapToPageResponseDomain(
                petRepository.findByActivationStatus_IsActiveAndOwner_Id(isActive, customerId, PageRequest.of(page, pageSize))
        );
    }

    public Pet findById(String id) {
        return mapper.mapToDomain(petRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0007)
        ));
    }

    public Pet insert(Pet pet, MultipartFile file) {
        customerRepository.findById(pet.getOwner().getId()).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );

        PetEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            pet.setId(null);
            pet.setActivationStatus(activationStatus);
            PetEntity petEntity = mapper.mapToEntity(pet);
            petEntity.setPathImage(uploadManager.uploadObject(file));
            saved = petRepository.save(petEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Pet update(String id, Pet petNew, MultipartFile file) {
        PetEntity petOld = petRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0007)
        );
        petOld.setName(petNew.getName() == null ? petOld.getName() : petNew.getName());
        petOld.setRace(petNew.getRace() == null ? petOld.getRace() : petNew.getRace());
        petOld.setGender(petNew.getGender() == null ? petOld.getGender() : petNew.getGender());
        petOld.setSpecies(petNew.getSpecies() == null ? petOld.getSpecies() : petNew.getSpecies());
        petOld.setSize(petNew.getSize() == null ? petOld.getSize() : petNew.getSize());
        petOld.setBirthDate(petNew.getBirthDate() == null ? petOld.getBirthDate() : petNew.getBirthDate());
        petOld.setNote(petNew.getNote() == null ? petOld.getNote() : petNew.getNote());
        if (file != null) petOld.setPathImage(uploadManager.uploadObject(file));
        PetEntity updated;
        try {
            updated = petRepository.save(petOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        PetEntity pet = petRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0007)
        );
        pet.getActivationStatus().setIsActive(false);
        pet.getActivationStatus().setDeactivationDate(Instant.now());
        petRepository.save(pet);
    }

    public void delete(String id) {
        PetEntity pet = petRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0007)
        );
        petRepository.delete(pet);
    }
}
