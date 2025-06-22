package com.bemtivi.bemtivi.controllers.in.comment.dto;

import com.bemtivi.bemtivi.application.enums.TypeComment;
import com.bemtivi.bemtivi.controllers.in.ActivationStatusDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.AdministratorDTO;
import com.bemtivi.bemtivi.controllers.in.customer.dto.CustomerDTO;
import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import com.bemtivi.bemtivi.controllers.in.service.dto.ServiceDTO;
import jakarta.validation.constraints.*;

public record CommentDTO(
        String id,
        @NotNull(groups = {OnCreate.class}, message = "O campo título deve ser preenchido.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, message = "O campo título está muito curto.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 50, message = "O campo título está muito longo.")
        String title,
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 600, message = "O campo mensagem deve ter no máximo 600 caracteres")
        String message,
        @NotNull(groups = {OnCreate.class}, message = "O campo tipo de comentário deve ser preenchido.")
        TypeComment typeComment,
        @NotNull(groups = {OnCreate.class}, message = "O campo avaliação deve ser preenchida.")
        @Min(groups = {OnCreate.class, OnUpdate.class}, value = 1, message = "O menor valor da avaliação é 1")
        @Max(groups = {OnCreate.class, OnUpdate.class}, value = 5, message = "O maior valor da avaliação é 5")
        Double rate,
        ProductDTO product,
        ServiceDTO service,
        @NotNull(groups = {OnCreate.class}, message = "O campo cliente deve ser preenchido.")
        CustomerDTO customer,
        ActivationStatusDTO activationStatus
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
