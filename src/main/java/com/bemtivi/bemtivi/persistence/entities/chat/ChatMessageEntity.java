package com.bemtivi.bemtivi.persistence.entities.chat;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_chat")
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "chat_id")
    private String id;
    @Column(name = "usuario_id", nullable = false)
    private String userId;
    @Column(name = "usuario_nome", nullable = false)
    private String userName;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "enviador", nullable = false)
    private UserRoleEnum sender;
    @Column(name = "conteudo", nullable = false)
    private String content;
}
