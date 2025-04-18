package com.bemtivi.bemtivi.services;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.product.Product;
import com.bemtivi.bemtivi.exceptions.DataIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import com.bemtivi.bemtivi.persistence.mappers.ProductPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductPersistenceMapper mapper;

    public PageResponse<Product> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                productRepository.findByPagination(isActive, PageRequest.of(page, pageSize), name == null ? "" : name)
        );
    }

    public Product findById(String id) {
        return mapper.mapToDomain(productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0001)
        ));
    }

    public Product insert(Product product) {
        ProductEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            product.setId(null);
            product.setActivationStatus(activationStatus);
            ProductEntity productEntity = mapper.mapToEntity(product);
            System.out.println(productEntity);
            saved = productRepository.save(productEntity);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0003);
        }
        return mapper.mapToDomain(saved);
    }

    public Product update(String id, Product productNew) {
        ProductEntity productOld = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0001)
        );
        productOld.setName(productNew.getName() == null ? productOld.getName() : productNew.getName());
        productOld.setPathImage(productNew.getPathImage() == null ? productOld.getPathImage() : productNew.getPathImage());
        productOld.setPrice(productNew.getPrice() == null ? productOld.getPrice() : productNew.getPrice());
        productOld.setDescription(productNew.getDescription() == null ? productOld.getDescription() : productNew.getDescription());
        ProductEntity updated;
        try {
            updated = productRepository.save(productOld);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0003);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0001)
        );
        product.getActivationStatus().setIsActive(false);
        product.getActivationStatus().setDeactivationDate(Instant.now());
        productRepository.save(product);
    }

    public void delete(String id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0001)
        );
        productRepository.delete(product);
    }
}
