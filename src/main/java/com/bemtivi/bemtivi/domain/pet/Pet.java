package com.bemtivi.bemtivi.domain.pet;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.customer.Customer;
import com.bemtivi.bemtivi.domain.enums.PetGenderEnum;
import com.bemtivi.bemtivi.domain.enums.PetSizeEnum;
import jakarta.persistence.Column;
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
