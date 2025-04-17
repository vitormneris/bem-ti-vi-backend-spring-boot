package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.domain.Product;
import com.bemtivi.bemtivi.persistence.entities.ProductEntity;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    Set<Product> mapToSetDomain(Set<ProductEntity> productEntity);
    Set<ProductEntity> mapToSetEntity(Set<Product> product);
    Product mapToDomain(ProductEntity productEntity);
    ProductEntity mapToEntity(Product product);
}
