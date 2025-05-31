package com.bemtivi.bemtivi.application.domain.user;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {
    @EqualsAndHashCode.Include
    protected String id;
    protected String name;
    protected String email;
    protected String pathImage;
    protected UserRoleEnum role;
    protected String password;
    protected ActivationStatus activationStatus;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role != null) {
            if (role.equals(UserRoleEnum.ADMINISTRATOR))
                return List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"), new SimpleGrantedAuthority("ROLE_CLIENT"));
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
