package com.bemtivi.bemtivi.persistence.entities.customer;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_enderecos")
public class AddressEntity {
    @Id
    @Column(name = "endereco_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "estado", nullable = false, length = 100)
    private String state;
    @Column(name = "cidade", nullable = false, length = 100)
    private String city;
    @Column(name = "bairro", nullable = false, length = 100)
    private String neighborhood;
    @Column(name = "rua", nullable = false, length = 100)
    private String street;
    @Column(name = "numero", nullable = false)
    private Integer number;
    @Column(name = "complemento", length = 100)
    private String complement;
    @Column(name = "cep", nullable = false, length = 9)
    private String postalCode;
}
