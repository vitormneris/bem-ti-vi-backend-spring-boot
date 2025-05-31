package com.bemtivi.bemtivi.application.business.product;

import com.bemtivi.bemtivi.application.business.UploadBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.category.Category;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import com.bemtivi.bemtivi.persistence.mappers.CategoryPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryBusiness {
    private final CategoryRepository categoryRepository;
    private final CategoryPersistenceMapper mapper;
    private final UploadBusiness uploadManager;

    public PageResponse<Category> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                categoryRepository.findByActivationStatus_IsActiveAndNameContainingIgnoreCase(isActive, name == null ? "" : name, PageRequest.of(page, pageSize))
        );
    }

    public Set<Category> findAll() {
        return mapper.mapToSetCategoryDomain(new LinkedHashSet<>(categoryRepository.findAll()));
    }

    public Category findById(String id) {
        return mapper.mapToDomain(categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0004)
        ));
    }

    public Category insert(Category category, MultipartFile file) {
        CategoryEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            category.setId(null);
            category.setActivationStatus(activationStatus);
            CategoryEntity categoryEntity = mapper.mapToEntity(category);
            categoryEntity.setPathImage(uploadManager.uploadObject(file));
            saved = categoryRepository.save(categoryEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Category update(String id, Category categoryNew, MultipartFile file) {
        CategoryEntity categoryOld = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0004)
        );
        categoryOld.setName(categoryNew.getName() == null ? categoryOld.getName() : categoryNew.getName());
        categoryOld.setCardColor(categoryNew.getCardColor() == null ? categoryOld.getCardColor() : categoryNew.getCardColor());
        if (file != null) categoryOld.setPathImage(uploadManager.uploadObject(file));
        CategoryEntity updated;
        try {
            updated = categoryRepository.save(categoryOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0004)
        );
        category.getActivationStatus().setIsActive(false);
        category.getActivationStatus().setDeactivationDate(Instant.now());
        categoryRepository.save(category);
    }

    public void delete(String id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0004)
        );
        categoryRepository.delete(category);
    }
}
