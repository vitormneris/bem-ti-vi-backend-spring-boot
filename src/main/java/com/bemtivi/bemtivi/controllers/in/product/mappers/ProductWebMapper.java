package com.bemtivi.bemtivi.controllers.in.product.mappers;

import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.PageResponse;
import com.bemtivi.bemtivi.domain.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductWebMapper {
    PageResponseDTO<ProductDTO> mapToPageResponseDto(PageResponse<Product> pageResponse);
    ProductDTO mapToDTO(Product product);
    Product mapToDomain(ProductDTO productDTO);
    ActivationStatusDTO mapToEntity(ActivationStatus activationStatus);
    ActivationStatus mapToDomain(ActivationStatusDTO activationStatus);
}
