package com.bemtivi.bemtivi.application.domain.customer;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.application.domain.order.Order;
import com.bemtivi.bemtivi.application.domain.pet.Pet;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String pathImage;
    private Telephone telephones;
    private Address address;
    private Set<Pet> pets;
    private Set<Order> orders;
    private Set<Appointment> appointments;
    private ActivationStatus activationStatus;
}
