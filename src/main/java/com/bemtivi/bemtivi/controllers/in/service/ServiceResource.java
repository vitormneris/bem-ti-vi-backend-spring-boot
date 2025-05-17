package com.bemtivi.bemtivi.controllers.in.service;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.service.dto.ServiceDTO;
import com.bemtivi.bemtivi.controllers.in.service.mappers.ServiceWebMapper;
import com.bemtivi.bemtivi.application.business.ServiceBusiness;
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
public class ServiceResource {
    private final ServiceBusiness serviceManager;
    private final ServiceWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<ServiceDTO>> paginate(
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
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(serviceManager.paginate(isActive, pageSize, page, name)));
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<ServiceDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(serviceManager.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<ServiceDTO> insert(
            @Validated(ServiceDTO.OnCreate.class) @RequestPart(value = "service") ServiceDTO serviceDTO,
            @Valid @NotNull(message = "A imagem deve ser enviada.") @RequestPart(value = "file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(serviceManager.insert(mapper.mapToDomain(serviceDTO), file))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<ServiceDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(ServiceDTO.OnUpdate.class) @RequestPart(value = "service") ServiceDTO serviceDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(serviceManager.update(id, mapper.mapToDomain(serviceDTO), file))
        );
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        serviceManager.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        serviceManager.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
