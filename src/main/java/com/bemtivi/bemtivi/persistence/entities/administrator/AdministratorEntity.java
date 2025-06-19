package com.bemtivi.bemtivi.persistence.entities.administrator;


import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
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
    @Enumerated(value = EnumType.STRING)
    @Column(name = "papel_do_usuario", nullable = false)
    private UserRoleEnum role;
    @Column(name = "caminho_da_imagem", nullable = false)
    private String pathImage;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name="email_esta_ativo" , nullable = false)
    private Boolean isEmailActive;
    @Column(name = "codigo")
    private String code;
    @Column(name = "email_pretendido")
    private String emailIntended;
    @Column(name = "senha", nullable = false, length = 100)
    private String password;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
