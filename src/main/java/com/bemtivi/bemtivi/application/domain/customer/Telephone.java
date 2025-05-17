package com.bemtivi.bemtivi.application.domain.customer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Telephone {
    @EqualsAndHashCode.Include
    private String id;
    private String phoneOne;
    private String phoneTwo;
}
