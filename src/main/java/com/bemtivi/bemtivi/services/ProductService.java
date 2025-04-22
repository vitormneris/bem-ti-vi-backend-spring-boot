package com.bemtivi.bemtivi.services;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.product.Product;
import com.bemtivi.bemtivi.exceptions.DataIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import com.bemtivi.bemtivi.persistence.mappers.ProductPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.CategoryRepository;
import com.bemtivi.bemtivi.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UploadService uploadService;
    private final ProductPersistenceMapper mapper;

    public PageResponse<Product> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                productRepository.findByPagination(isActive, PageRequest.of(page, pageSize), name == null ? "" : name)
        );
    }

    public Product findById(String id) {
        return mapper.mapToDomain(productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        ));
    }

    public Product insert(Product product, MultipartFile file) {
        ProductEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            product.setId(null);
            product.setActivationStatus(activationStatus);
            product.getCategories().forEach(category -> categoryRepository.findById(category.getId()).orElseThrow(
                    () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0004)
            ));
            ProductEntity productEntity = mapper.mapToEntity(product);
            productEntity.setPathImage(uploadService.uploadObject(file));
            saved = productRepository.save(productEntity);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Product update(String id, Product productNew, MultipartFile file) {
        ProductEntity productOld = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        );
        productOld.setName(productNew.getName() == null ? productOld.getName() : productNew.getName());
        productOld.setPrice(productNew.getPrice() == null ? productOld.getPrice() : productNew.getPrice());
        productOld.setDescription(productNew.getDescription() == null ? productOld.getDescription() : productNew.getDescription());
        if (file != null) productOld.setPathImage(uploadService.uploadObject(file));
        if (productNew.getCategories() != null) {
            productNew.getCategories().forEach(category -> categoryRepository.findById(category.getId()).orElseThrow(
                    () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0004)
            ));
            productOld.setCategories( mapper.mapToSetEntity(productNew.getCategories()) );
        }
        ProductEntity updated;
        try {
            updated = productRepository.save(productOld);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        );
        product.getActivationStatus().setIsActive(false);
        product.getActivationStatus().setDeactivationDate(Instant.now());
        productRepository.save(product);
    }

    public void delete(String id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        );
        productRepository.delete(product);
    }
}
