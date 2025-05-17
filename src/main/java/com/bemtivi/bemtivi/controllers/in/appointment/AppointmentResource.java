package com.bemtivi.bemtivi.controllers.in.appointment;

import com.bemtivi.bemtivi.application.business.AppointmentBusiness;
import com.bemtivi.bemtivi.controllers.in.appointment.dto.AppointmentDTO;
import com.bemtivi.bemtivi.controllers.in.appointment.mappers.AppointmentWebMapper;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/agendamento")
public class AppointmentResource {
    private final AppointmentBusiness appointmentManager;
    private final AppointmentWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<AppointmentDTO>> paginate(
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
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(appointmentManager.paginate(isActive, pageSize, page, name)));
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<AppointmentDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(appointmentManager.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<AppointmentDTO> insert(
            @Validated(AppointmentDTO.OnCreate.class) @RequestBody AppointmentDTO appointmentDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(appointmentManager.insert(mapper.mapToDomain(appointmentDTO)))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<AppointmentDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(AppointmentDTO.OnUpdate.class) @RequestBody AppointmentDTO appointmentDTO
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(appointmentManager.update(id, mapper.mapToDomain(appointmentDTO)))
        );
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        appointmentManager.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        appointmentManager.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

