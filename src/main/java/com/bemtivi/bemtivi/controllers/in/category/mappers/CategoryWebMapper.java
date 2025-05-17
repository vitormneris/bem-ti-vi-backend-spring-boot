package com.bemtivi.bemtivi.controllers.in.category.mappers;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.category.dto.CategoryDTO;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.category.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryWebMapper {
    PageResponseDTO<CategoryDTO> mapToPageResponseDto(PageResponse<Category> pageResponse);
    CategoryDTO mapToDTO(Category category);
    Category mapToDomain(CategoryDTO categoryDTO);
}
