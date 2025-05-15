package com.bemtivi.bemtivi.controllers.in.order;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.order.dto.OrderDTO;
import com.bemtivi.bemtivi.controllers.in.order.mappers.OrderWebMapper;
import com.bemtivi.bemtivi.managers.OrderManager;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pedido")
public class OrderResource {
    private final OrderManager orderManager;
    private final OrderWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<OrderDTO>> paginate(
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
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(orderManager.paginate(isActive, pageSize, page, name)));
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<OrderDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(orderManager.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<OrderDTO> insert(
            @Validated(OrderDTO.OnCreate.class) @RequestBody OrderDTO orderDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(orderManager.insert(mapper.mapToDomain(orderDTO)))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<OrderDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(OrderDTO.OnUpdate.class) @RequestBody OrderDTO orderDTO
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(orderManager.update(id, mapper.mapToDomain(orderDTO)))
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

