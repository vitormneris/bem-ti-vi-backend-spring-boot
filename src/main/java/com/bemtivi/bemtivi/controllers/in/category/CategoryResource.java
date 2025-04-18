package com.bemtivi.bemtivi.controllers.in.category;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.category.dto.CategoryDTO;
import com.bemtivi.bemtivi.controllers.in.category.mappers.CategoryWebMapper;
import com.bemtivi.bemtivi.services.CategoryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categoria")
public class CategoryResource {
    private final CategoryService categoryService;
    private final CategoryWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<CategoryDTO>> paginate(
            @RequestParam(name = "isActive", defaultValue = "true", required = false)
            Boolean isActive,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false)
            @Min(value = 1, message = "O número mínimo de elementos da página é 1")
            @Max(value = 30, message = "O número máximo de elementos da página é 30")
            Integer pageSize,
            @RequestParam(name = "page", defaultValue = "0", required = false)
            Integer page,
            @RequestParam(name = "name", required = false)
            String name
    ) {
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(categoryService.paginate(isActive, pageSize, page, name)));
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<CategoryDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(categoryService.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<CategoryDTO> insert(@Validated(Default.class) @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToDTO(categoryService.insert(mapper.mapToDomain(categoryDTO))));
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<CategoryDTO> update(@PathVariable(name = "id") String id, @Validated(CategoryDTO.Mandatory.class) @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok().body(mapper.mapToDTO(categoryService.update(id, mapper.mapToDomain(categoryDTO))));
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        categoryService.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
