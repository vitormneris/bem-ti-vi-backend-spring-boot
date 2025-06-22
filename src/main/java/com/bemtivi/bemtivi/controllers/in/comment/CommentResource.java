package com.bemtivi.bemtivi.controllers.in.comment;

import com.bemtivi.bemtivi.application.business.comment.CommentBusiness;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import com.bemtivi.bemtivi.controllers.in.comment.dto.CommentDTO;
import com.bemtivi.bemtivi.controllers.in.comment.mappers.CommentWebMapper;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/comentarios")
public class CommentResource {
    private final CommentBusiness commentBusiness;
    private final CommentWebMapper mapper;

    @GetMapping(value = "/buscarporidservico")
    public ResponseEntity<PageResponseDTO<CommentDTO>> findByServiceId(
            @RequestParam(name = "isActive", defaultValue = "true", required = false)
            Boolean isActive,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false)
            @Min(value = 1, message = "O número mínimo de elementos da página é 1")
            @Max(value = 30, message = "O número máximo de elementos da página é 30")
            Integer pageSize,
            @RequestParam(name = "page", defaultValue = "0", required = false)
            Integer page,
            @RequestParam(name = "serviceId")
            String serviceId
    ) {
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(commentBusiness.findByServiceId(isActive, pageSize, page, serviceId)));
    }

    @GetMapping(value = "/buscarporidproduto")
    public ResponseEntity<PageResponseDTO<CommentDTO>> findByProductId(
            @RequestParam(name = "isActive", defaultValue = "true", required = false)
            Boolean isActive,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false)
            @Min(value = 1, message = "O número mínimo de elementos da página é 1")
            @Max(value = 30, message = "O número máximo de elementos da página é 30")
            Integer pageSize,
            @RequestParam(name = "page", defaultValue = "0", required = false)
            Integer page,
            @RequestParam(name = "productId")
            String productId
    ) {
        return ResponseEntity.ok().body(mapper.mapToPageResponseDto(commentBusiness.findByProductId(isActive, pageSize, page, productId)));
    }


    @PostMapping(value = "/inserir")
    public ResponseEntity<CommentDTO> insert(
            @Validated(CommentDTO.OnCreate.class) @RequestBody CommentDTO commentDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.mapToDTO(commentBusiness.insert(mapper.mapToDomain(commentDTO)))
        );
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<CommentDTO> update(
            @PathVariable(name = "id") String id,
            @Validated(CommentDTO.OnUpdate.class) @RequestBody CommentDTO commentDTO
    ) {
        return ResponseEntity.ok().body(
                mapper.mapToDTO(commentBusiness.update(id, mapper.mapToDomain(commentDTO)))
        );
    }

    @DeleteMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> deactivate(@PathVariable(name = "id") String id) {
        commentBusiness.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        commentBusiness.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
