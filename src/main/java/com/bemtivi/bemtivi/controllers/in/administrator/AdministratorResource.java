package com.bemtivi.bemtivi.controllers.in.administrator;

import com.bemtivi.bemtivi.application.business.user.AdministratorBusiness;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.AdministratorDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.mappers.AdministratorWebMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/administradores")
public class AdministratorResource {
    private final AdministratorBusiness administratorBusiness;
    private final AdministratorWebMapper mapper;

    @GetMapping(value = "/buscartodos")
    public ResponseEntity<Set<AdministratorDTO>> findAll() {
        return ResponseEntity.ok().body(
                mapper.mapToSetDTO(administratorBusiness.findAll())
        );
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<AdministratorDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(administratorBusiness.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<AdministratorDTO> insert(
            @Validated(AdministratorDTO.OnCreate.class) @RequestPart(value = "administrator") AdministratorDTO administratorDTO,
            @Valid @NotNull(message = "A imagem deve ser enviada.") @RequestPart(value = "file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(administratorBusiness.insert(mapper.mapToDomain(administratorDTO), file))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<AdministratorDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(AdministratorDTO.OnUpdate.class) @RequestPart(value = "administrator") AdministratorDTO administratorDTO,
            @Valid @NotNull(message = "A imagem deve ser enviada.") @RequestPart(value = "file") MultipartFile file
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(administratorBusiness.update(id, mapper.mapToDomain(administratorDTO), file))
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
