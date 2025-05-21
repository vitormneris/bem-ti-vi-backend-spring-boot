package com.bemtivi.bemtivi.controllers.auth;

import com.bemtivi.bemtivi.application.business.TokenBusiness;
import com.bemtivi.bemtivi.application.domain.user.User;
import com.bemtivi.bemtivi.controllers.auth.dto.AuthorizationDTO;
import com.bemtivi.bemtivi.controllers.auth.dto.TokenDTO;
import com.bemtivi.bemtivi.controllers.auth.dto.UserIdDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autorizacao")
@RequiredArgsConstructor
public class AuthorizationResource {
    private final AuthenticationManager authenticationManager;
    private final TokenBusiness tokenBusiness;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> authenticate(@RequestBody AuthorizationDTO data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var user = (User) authenticate.getPrincipal();
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