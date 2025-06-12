package com.bemtivi.bemtivi.controllers.in.comment.dto;

import com.bemtivi.bemtivi.application.enums.TypeComment;
import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import com.bemtivi.bemtivi.controllers.in.service.dto.ServiceDTO;
import jakarta.validation.constraints.*;

public record CommentDTO(
        String id,
        @NotBlank(groups = {OnCreate.class}, message = "O título deve ser preenchido.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 50, message = "O título deve ter entre 3 e 50 caracteres")
        String title,
        @NotBlank(groups = {OnCreate.class}, message = "A mensagem deve ser preenchida.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 150, message = "A mensagem deve ter no máximo 50 caracteres")
        String message,
        @NotNull(groups = {OnCreate.class}, message = "O tipo de comentário deve ser preenchido.")
        TypeComment typeComment,
        @NotNull(groups = {OnCreate.class}, message = "A nota deve ser preenchida.")
        @Min(groups = {OnCreate.class, OnUpdate.class}, value = 0, message = "O menor valor do nota é 0")
        @Max(groups = {OnCreate.class, OnUpdate.class}, value = 5, message = "O maior valor do nota é 5")
        Double rate,
        ProductDTO product,
        ServiceDTO service,
        @NotNull(groups = {OnCreate.class}, message = "O cliente deve ser informado.")
        CustomerDTO customer,
        ActivationStatusDTO activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
