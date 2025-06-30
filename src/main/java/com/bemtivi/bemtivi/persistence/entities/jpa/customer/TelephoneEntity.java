package com.bemtivi.bemtivi.persistence.entities.jpa.customer;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_telefones")
public class TelephoneEntity {
    @Id
    @Column(name = "telefone_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "telefone1", nullable = false, length = 15)
    private String phoneOne;
    @Column(name = "telefone2", length = 15)
    private String phoneTwo;
}
