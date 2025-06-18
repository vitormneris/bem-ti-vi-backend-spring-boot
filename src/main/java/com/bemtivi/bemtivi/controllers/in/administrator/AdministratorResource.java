package com.bemtivi.bemtivi.controllers.in.administrator;

import com.bemtivi.bemtivi.application.business.user.AdministratorBusiness;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.AdministratorDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.PasswordsDTO;
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

    @GetMapping(value = "/{active}/buscarporstatus")
    public ResponseEntity<Set<AdministratorDTO>> findByActivationStatus(@PathVariable(name = "active") Boolean active) {
        return ResponseEntity.ok().body(
                mapper.mapToSetDTO(administratorBusiness.findByActivationStatus(active))
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

    @PatchMapping(value = "/{id}/atualizarsenha")
    public ResponseEntity<Void> updatePassword(
            @PathVariable(name = "id") String id, @Validated(PasswordsDTO.OnCreate.class) @RequestBody PasswordsDTO passwords
    ) {
        administratorBusiness.updatePassword(id, passwords);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}/solicitartrocaemail")
    public ResponseEntity<Void> sendRequestEmail(
            @PathVariable(name = "id") String id, @Validated(UserAuthDTO.OnUpdate.class) @RequestBody UserAuthDTO user
    ) {
        administratorBusiness.sendRequestEmail(id, user.email());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}/atualizaremail/{code}")
    public ResponseEntity<Void> updateEmail(@PathVariable(name = "id") String id, @PathVariable(name = "code") String code) {
        administratorBusiness.updateEmail(id, code);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}/solicitarconfirmacaoemail")
    public ResponseEntity<Void> sendRequestConfirmationEmail(
            @PathVariable(name = "id") String id, @Validated(UserAuthDTO.OnUpdate.class) @RequestBody UserAuthDTO user
    ) {
        administratorBusiness.sendRequestConfirmationEmail(id, user.email());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}/confirmacaoemail/{code}")
    public ResponseEntity<Void> confirmationEmail(@PathVariable(name = "id") String id, @PathVariable(name = "code") String code) {
        administratorBusiness.confirmationEmail(id, code);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        administratorBusiness.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/{id}/ativar")
    public ResponseEntity<Void> activate(@PathVariable(name = "id") String id) {
        administratorBusiness.activate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "id") String id, @Validated(UserAuthDTO.OnUpdate.class) @RequestBody UserAuthDTO user
    ) {
        administratorBusiness.delete(id, user.password());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
