package com.bemtivi.bemtivi.application.domain.user;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User implements UserDetails {
    @EqualsAndHashCode.Include
    protected String id;
    protected String name;
    protected String email;
    protected String password;
    private ActivationStatus activationStatus;
}
