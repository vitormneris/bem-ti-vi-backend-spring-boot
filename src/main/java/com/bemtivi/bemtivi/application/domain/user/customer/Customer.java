package com.bemtivi.bemtivi.application.domain.user.customer;

import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.application.domain.order.Order;
import com.bemtivi.bemtivi.application.domain.pet.Pet;
import com.bemtivi.bemtivi.application.domain.user.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class Customer extends User {
    private LocalDate birthDate;
    private String pathImage;
    private Telephone telephones;
    private Address address;
    private Set<Pet> pets;
    private Set<Order> orders;
    private Set<Appointment> appointments;
}
