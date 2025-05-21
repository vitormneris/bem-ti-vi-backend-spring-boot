package com.bemtivi.bemtivi.configuration.auth;

import com.bemtivi.bemtivi.application.business.TokenBusiness;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.mappers.AdministratorPersistenceMapper;
import com.bemtivi.bemtivi.persistence.mappers.CustomerPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.AdministratorRepository;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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
                        .orElseThrow(() -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0011))
                );
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}