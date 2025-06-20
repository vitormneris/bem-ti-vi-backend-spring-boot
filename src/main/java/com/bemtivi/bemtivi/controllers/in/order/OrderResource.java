package com.bemtivi.bemtivi.controllers.in.order;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.OrderDTO;
import com.bemtivi.bemtivi.controllers.in.order.mappers.OrderWebMapper;
import com.bemtivi.bemtivi.application.business.product.OrderBusiness;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pedidos")
public class OrderResource {
    private final OrderBusiness orderManager;
    private final OrderWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<OrderDTO>> findByPagination(
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
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(orderManager.findByPagination(isActive, pageSize, page, momentStart, momentEnd)));
    }

    @GetMapping(value = "/paginacaoporcliente")
    public ResponseEntity<PageResponseDTO<OrderDTO>> paginate(
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
            @RequestParam(name = "paymentStatus", defaultValue = "", required = false)
            String paymentStatus
    ) {
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(
                orderManager.findByActivationStatusIsActiveAndCustomerIdAndPaymentStatus(isActive, customerId, paymentStatus, pageSize, page)
        ));
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<OrderDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(orderManager.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<OrderDTO> insert(
            @Validated(OrderDTO.OnCreate.class) @RequestBody OrderDTO orderDTO
    ) {

        log.warn(orderDTO.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(orderManager.insert(mapper.mapToDomain(orderDTO)))
        );
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        orderManager.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        orderManager.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

