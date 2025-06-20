package com.bemtivi.bemtivi.controllers.in.appointment;

import com.bemtivi.bemtivi.application.business.service.AppointmentBusiness;
import com.bemtivi.bemtivi.controllers.in.appointment.dto.AppointmentDTO;
import com.bemtivi.bemtivi.controllers.in.appointment.mappers.AppointmentWebMapper;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.OrderDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/agendamentos")
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
            @RequestParam(name = "momentStart", required = false)
            LocalDate momentStart,
            @RequestParam(name = "momentEnd", required = false)
            LocalDate momentEnd
    ) {
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(appointmentManager.findByPagination(isActive, pageSize, page, momentStart, momentEnd)));
    }

    @GetMapping(value = "/paginacaoporcliente")
    public ResponseEntity<PageResponseDTO<AppointmentDTO>> paginate(
            @RequestParam(name = "isActive", defaultValue = "true", required = false)
            Boolean isActive,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false)
            @Min(value = 1, message = "O número mínimo de elementos da página é 1")
            @Max(value = 30, message = "O número máximo de elementos da página é 30")
            Integer pageSize,
            @RequestParam(name = "page", defaultValue = "0", required = false)
            Integer page,
            @Valid @NotNull(message = "O ID do cliente não pode ser nulo")
            @RequestParam(name = "customerId")
            String customerId,
            @RequestParam(name = "paymentStatus", required = false)
            String paymentStatus
    ) {
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(
                appointmentManager.findByActivationStatusIsActiveAndCustomerIdAndPaymentStatus(isActive, customerId, paymentStatus, pageSize, page)
        ));
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

