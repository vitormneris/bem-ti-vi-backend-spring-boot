package com.bemtivi.bemtivi.application.domain.customer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {
    @EqualsAndHashCode.Include
    private String id;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private Integer number;
    private String complement;
    private String postalCode;
}
