package com.bemtivi.bemtivi.persistence.entities.chat;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT-3")
    @Column(name = "momento", nullable = false)
    private Instant moment;
}
