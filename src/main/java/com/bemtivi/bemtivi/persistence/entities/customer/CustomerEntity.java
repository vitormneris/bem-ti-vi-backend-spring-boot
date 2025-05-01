package com.bemtivi.bemtivi.persistence.entities.customer;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.pet.PetEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_clientes")
public class CustomerEntity {
    @Id
    @Column(name = "cliente_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "nome", nullable = false, length = 100)
    private String name;
    @NotNull
    private String email;
    @Column(name = "senha", nullable = false, length = 100)
    private String password;
    @Column(name = "data_de_nascimento", nullable = false)
    private LocalDate birthDate;
    @Column(name = "caminho_da_imagem", nullable = false)
    private String pathImage;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "telefone_id", referencedColumnName = "telefone_id")
    private TelephoneEntity telephones;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", referencedColumnName = "endereco_id")
    private AddressEntity address;
    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Set<PetEntity> pets;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
