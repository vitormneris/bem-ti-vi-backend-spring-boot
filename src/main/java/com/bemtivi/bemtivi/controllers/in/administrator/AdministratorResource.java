package com.bemtivi.bemtivi.controllers.in.administrator;

import com.bemtivi.bemtivi.application.business.AdministratorBusiness;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.AdministratorDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.mappers.AdministratorWebMapper;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/administrador")
public class AdministratorResource {
    private final AdministratorBusiness administratorBusiness;
    private final AdministratorWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<AdministratorDTO>> paginate(
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
        return ResponseEntity.ok().body(
                mapper.mapToPageResponseDto(administratorBusiness.paginate(isActive, pageSize, page, name))
        );
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<AdministratorDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(administratorBusiness.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<AdministratorDTO> insert(@Validated(ProductDTO.OnCreate.class) @RequestBody AdministratorDTO administratorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(administratorBusiness.insert(mapper.mapToDomain(administratorDTO)))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<AdministratorDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(AdministratorDTO.OnUpdate.class) @RequestBody AdministratorDTO administratorDTO
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(administratorBusiness.update(id, mapper.mapToDomain(administratorDTO)))
        );
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        administratorBusiness.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        administratorBusiness.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
