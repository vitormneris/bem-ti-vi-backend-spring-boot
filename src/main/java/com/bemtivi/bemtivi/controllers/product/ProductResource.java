package com.bemtivi.bemtivi.controllers.product;

import com.bemtivi.bemtivi.controllers.product.dto.ProductDTO;
import com.bemtivi.bemtivi.controllers.product.mappers.ProductWebMapper;
import com.bemtivi.bemtivi.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/produto")
public class ProductResource {
    private final ProductService productService;
    private final ProductWebMapper mapper;

    @GetMapping(value = "/buscartodos")
    public ResponseEntity<Set<ProductDTO>> findAll() {
        return ResponseEntity.ok().body(mapper.mapToSetDTO(productService.findAll()));
    }

    @GetMapping(value = "/{id}/buscar")
    public ResponseEntity<ProductDTO> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().body(mapper.mapToDTO(productService.findById(id)));
    }

    @PostMapping(value = "/inserir")
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToDTO(productService.insert(mapper.mapToDomain(productDTO))));
    }

    @PutMapping(value = "/{id}/atualizar")
    public ResponseEntity<ProductDTO> update(@PathVariable(name = "id") String id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok().body(mapper.mapToDTO(productService.update(id, mapper.mapToDomain(productDTO))));
    }

    @DeleteMapping(value = "/{id}/deletar")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
