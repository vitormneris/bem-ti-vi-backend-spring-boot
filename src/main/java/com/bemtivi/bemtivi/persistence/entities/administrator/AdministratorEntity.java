package com.bemtivi.bemtivi.persistence.entities.administrator;


import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_administradores")
public class AdministratorEntity {
    @Id
    @Column(name = "administrador_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "nome", nullable = false, length = 100)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "senha", nullable = false, length = 100)
    private String password;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
