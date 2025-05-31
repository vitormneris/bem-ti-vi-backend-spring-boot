package com.bemtivi.bemtivi.controllers.in.category.mappers;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.category.dto.CategoryDTO;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.category.Category;
import com.bemtivi.bemtivi.persistence.entities.category.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryWebMapper {
    PageResponseDTO<CategoryDTO> mapToPageResponseDto(PageResponse<Category> pageResponse);
    Set<CategoryDTO> mapToSetCategoryDTO(Set<Category> categories);
    CategoryDTO mapToDTO(Category category);
    Category mapToDomain(CategoryDTO categoryDTO);
}
