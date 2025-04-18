package com.bemtivi.bemtivi.services;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.category.Category;
import com.bemtivi.bemtivi.exceptions.DataIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import com.bemtivi.bemtivi.persistence.mappers.CategoryPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryPersistenceMapper mapper;

    public PageResponse<Category> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                categoryRepository.findByPagination(isActive, PageRequest.of(page, pageSize), name == null ? "" : name)
        );
    }

    public Category findById(String id) {
        return mapper.mapToDomain(categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0001)
        ));
    }

    public Category insert(Category category) {
        CategoryEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            category.setId(null);
            category.setActivationStatus(activationStatus);
            CategoryEntity categoryEntity = mapper.mapToEntity(category);
            saved = categoryRepository.save(categoryEntity);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0003);
        }
        return mapper.mapToDomain(saved);
    }

    public Category update(String id, Category categoryNew) {
        CategoryEntity categoryOld = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0001)
        );
        categoryOld.setName(categoryNew.getName() == null ? categoryOld.getName() : categoryNew.getName());
        categoryOld.setPathImage(categoryNew.getPathImage() == null ? categoryOld.getPathImage() : categoryNew.getPathImage());
        categoryOld.setCardColor(categoryNew.getCardColor() == null ? categoryOld.getCardColor() : categoryNew.getCardColor());
        CategoryEntity updated;
        try {
            updated = categoryRepository.save(categoryOld);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0003);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0001)
        );
        category.getActivationStatus().setIsActive(false);
        category.getActivationStatus().setDeactivationDate(Instant.now());
        categoryRepository.save(category);
    }

    public void delete(String id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0001)
        );
        categoryRepository.delete(category);
    }
}
