package com.bemtivi.bemtivi.domain.category;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String cardColor;
    private String pathImage;
    private ActivationStatus activationStatus;
}
