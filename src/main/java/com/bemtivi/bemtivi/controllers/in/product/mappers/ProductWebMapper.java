package com.bemtivi.bemtivi.controllers.in.product.mappers;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductWebMapper {
    PageResponseDTO<ProductDTO> mapToPageResponseDto(PageResponse<Product> pageResponse);
    ProductDTO mapToDTO(Product product);
    Product mapToDomain(ProductDTO productDTO);
}
