package com.bemtivi.bemtivi.controllers.in.petservice;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.petservice.dto.PetServiceDTO;
import com.bemtivi.bemtivi.controllers.in.petservice.mappers.PetServiceWebMapper;
import com.bemtivi.bemtivi.services.PetServiceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service")
public class PetServiceResource {
    private final PetServiceService petServiceService;
    private final PetServiceWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<PetServiceDTO>> paginate(
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
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(petServiceService.paginate(isActive, pageSize, page, name)));
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<PetServiceDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(petServiceService.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<PetServiceDTO> insert(
            @Validated(PetServiceDTO.OnCreate.class) @RequestPart(value = "service") PetServiceDTO petServiceDTO,
            @Valid @NotNull(message = "A imagem deve ser enviada.") @RequestPart(value = "file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(petServiceService.insert(mapper.mapToDomain(petServiceDTO), file))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<PetServiceDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(PetServiceDTO.OnUpdate.class) @RequestPart(value = "service") PetServiceDTO petServiceDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(petServiceService.update(id, mapper.mapToDomain(petServiceDTO), file))
        );
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        petServiceService.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        petServiceService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
