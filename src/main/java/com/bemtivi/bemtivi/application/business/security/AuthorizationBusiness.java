package com.bemtivi.bemtivi.application.business.security;

import com.bemtivi.bemtivi.exceptions.AuthenticationFailedException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.mappers.AdministratorPersistenceMapper;
import com.bemtivi.bemtivi.persistence.mappers.CustomerPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.AdministratorRepository;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationBusiness implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    private final AdministratorRepository administratorRepository;
    private final AdministratorPersistenceMapper administratorPersistenceMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserAuthDTO> objCustomer = customerRepository.findByUsername(email);

        if (objCustomer.isPresent()) {
            return customerPersistenceMapper.mapUserAuthDTOToDomain(objCustomer.get());
        }
        return administratorPersistenceMapper.mapUserAuthDTOToDomain(administratorRepository.findByUsername(email).orElseThrow(
                () -> new AuthenticationFailedException(RuntimeErrorEnum.ERR0014)
        ));
    }
}