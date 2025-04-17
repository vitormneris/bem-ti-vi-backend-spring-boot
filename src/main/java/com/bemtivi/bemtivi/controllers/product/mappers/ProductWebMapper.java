package com.bemtivi.bemtivi.controllers.product.mappers;

import com.bemtivi.bemtivi.controllers.product.dto.ProductDTO;
import com.bemtivi.bemtivi.domain.Product;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductWebMapper {
    Set<ProductDTO> mapToSetDTO(Set<Product> product);
    Set<Product> mapToSetDomain(Set<ProductDTO> productDTO);
    ProductDTO mapToDTO(Product product);
    Product mapToDomain(ProductDTO productDTO);
}
