package com.bemtivi.bemtivi.controllers.in.customer;

import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.controllers.in.customer.mappers.CustomerWebMapper;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import com.bemtivi.bemtivi.application.business.CustomerBusiness;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cliente")
public class CustomerResource {
    private final CustomerBusiness customerBusiness;
    private final CustomerWebMapper mapper;

    @GetMapping(value = "/paginacao")
    public ResponseEntity<PageResponseDTO<CustomerDTO>> paginate(
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
                mapper.mapToPageResponseDto(customerBusiness.paginate(isActive, pageSize, page, name))
        );
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<CustomerDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(customerBusiness.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<CustomerDTO> insert(
            @Validated(ProductDTO.OnCreate.class) @RequestPart(value = "customer") CustomerDTO customerDTO,
            @Valid @NotNull(message = "A imagem deve ser enviada.") @RequestPart(value = "file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(customerBusiness.insert(mapper.mapToDomain(customerDTO), file))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<CustomerDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(CustomerDTO.OnUpdate.class) @RequestPart(value = "customer") CustomerDTO customerDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(customerBusiness.update(id, mapper.mapToDomain(customerDTO), file))
        );
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        customerBusiness.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        customerBusiness.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
