package com.bemtivi.bemtivi.configuration.auth;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bemtivi.bemtivi.application.business.security.TokenBusiness;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.configuration.auth.dto.ErrorSecurityMessageDTO;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.exceptions.AuthenticationFailedException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.mappers.AdministratorPersistenceMapper;
import com.bemtivi.bemtivi.persistence.mappers.CustomerPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.AdministratorRepository;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilterToken extends OncePerRequestFilter {
    private final TokenBusiness tokenBusiness;
    private final CustomerRepository customerRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    private final AdministratorRepository administratorRepository;
    private final AdministratorPersistenceMapper administratorPersistenceMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

       try {
           if (authorizationHeader != null) {
               String token = authorizationHeader.replace("Bearer ", "");
               String email = tokenBusiness.getSubject(token);

               Optional<UserAuthDTO> objCustomer = customerRepository.findByUsername(email);
               if (objCustomer.isPresent()) {
                   Customer user = customerPersistenceMapper.mapUserAuthDTOToDomain(objCustomer.get());
                   var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                   SecurityContextHolder.getContext().setAuthentication(authentication);
               } else {
                   Administrator user = administratorPersistenceMapper.mapUserAuthDTOToDomain(administratorRepository.findByUsername(email)
                           .orElseThrow(() -> new BadCredentialsException(""))
                   );
                   var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
           }
           filterChain.doFilter(request, response);
       } catch (BadCredentialsException | TokenExpiredException | JWTDecodeException exception) {
           RuntimeErrorEnum runtimeErrorEnum = RuntimeErrorEnum.ERR0015;
           HttpStatus status = HttpStatus.FORBIDDEN;

           ErrorSecurityMessageDTO errorMessageDTO = ErrorSecurityMessageDTO.builder()
                   .code(runtimeErrorEnum.getCode())
                   .status(status.value())
                   .message(runtimeErrorEnum.getMessage())
                   .timestamp(new Date())
                   .path(request.getRequestURI())
                   .build();

           response.setStatus(status.value());
           response.setContentType(MediaType.APPLICATION_JSON_VALUE);
           new ObjectMapper().writeValue(response.getWriter(), errorMessageDTO);
       }
    }
}