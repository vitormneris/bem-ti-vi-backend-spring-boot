package com.bemtivi.bemtivi.application.domain.pet;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.enums.PetGenderEnum;
import com.bemtivi.bemtivi.application.enums.PetSizeEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pet {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private LocalDate birthDate;
    private String race;
    private PetSizeEnum size;
    private PetGenderEnum gender;
    private String pathImage;
    private String species;
    private String note;
    private Customer owner;
    private ActivationStatus activationStatus;
}
