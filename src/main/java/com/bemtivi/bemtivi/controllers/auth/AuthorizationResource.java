package com.bemtivi.bemtivi.controllers.auth;

import com.bemtivi.bemtivi.application.business.security.TokenBusiness;
import com.bemtivi.bemtivi.application.domain.user.User;
import com.bemtivi.bemtivi.controllers.auth.dto.AuthorizationDTO;
import com.bemtivi.bemtivi.controllers.auth.dto.TokenDTO;
import com.bemtivi.bemtivi.controllers.auth.dto.UserIdDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/autenticacao")
@RequiredArgsConstructor
public class AuthorizationResource {
    private final AuthenticationManager authenticationManager;
    private final TokenBusiness tokenBusiness;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> authenticate(@RequestBody AuthorizationDTO data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        User user = (User) authenticate.getPrincipal();
        return ResponseEntity.ok().body(new TokenDTO(tokenBusiness.tokenGeneration(user)));
    }

    @GetMapping(value = "/token/cliente")
    public ResponseEntity<UserIdDTO> validationCustomer(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok().body(new UserIdDTO(tokenBusiness.getClaimId(token)));
    }

    @GetMapping(value = "/token/administrador")
    public ResponseEntity<UserIdDTO> validationAdministrator(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok().body(new UserIdDTO(tokenBusiness.getClaimId(token)));
    }
}