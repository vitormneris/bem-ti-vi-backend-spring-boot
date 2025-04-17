package com.bemtivi.bemtivi.services;

import com.bemtivi.bemtivi.domain.Product;
import com.bemtivi.bemtivi.persistence.entities.ProductEntity;
import com.bemtivi.bemtivi.persistence.mappers.ProductPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductPersistenceMapper mapper;

    public Set<Product> findAll() {
        return mapper.mapToSetDomain(new LinkedHashSet<>(productRepository.findAll()));
    }

    public Product findById(String id) {
        return mapper.mapToDomain(productRepository.findById(id).orElseThrow());
    }

    public Product insert(Product product) {
        product.setId(null);
        return mapper.mapToDomain(productRepository.save(mapper.mapToEntity(product)));
    }

    public Product update(String id, Product productNew) {
        ProductEntity productOld = productRepository.findById(id).orElseThrow();
        productOld.setName(productNew.getName() == null ? productOld.getName() : productNew.getName());
        productOld.setPathImage(productNew.getPathImage() == null ? productOld.getPathImage() : productNew.getPathImage());
        productOld.setPrice(productNew.getPrice() == null ? productOld.getPrice() : productNew.getPrice());
        productOld.setDescription(productNew.getDescription() == null ? productOld.getDescription() : productNew.getDescription());
        return mapper.mapToDomain(productRepository.save(productOld));
    }

    public void delete(String id) {
        ProductEntity product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);
    }
}
