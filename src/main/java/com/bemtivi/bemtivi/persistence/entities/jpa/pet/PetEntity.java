package com.bemtivi.bemtivi.persistence.entities.jpa.pet;

import com.bemtivi.bemtivi.application.enums.PetGenderEnum;
import com.bemtivi.bemtivi.application.enums.PetSizeEnum;
import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.customer.CustomerEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_pets")
public class PetEntity {
    @Id
    @Column(name = "pet_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "nome", nullable = false, length = 100)
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "data_de_aniversario", nullable = false)
    private LocalDate birthDate;
    @Column(name = "raca", length = 50)
    private String race;
    @Column(name = "caminho_da_imagem", nullable = false)
    private String pathImage;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "porte", nullable = false)
    private PetSizeEnum size;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private PetGenderEnum gender;
    @Column(name = "especie", nullable = false, length = 50)
    private String species;
    @Column(name = "detalhes", length = 700)
    private String details;
    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false, referencedColumnName = "cliente_id")
    private CustomerEntity owner;
    @Embedded
    private ActivationStatusEntity activationStatus;
}

