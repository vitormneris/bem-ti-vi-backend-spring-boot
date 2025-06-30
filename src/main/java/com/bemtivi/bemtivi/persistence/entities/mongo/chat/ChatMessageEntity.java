package com.bemtivi.bemtivi.persistence.entities.mongo.chat;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "mensagens")
public class ChatMessageEntity {

    @Id
    @EqualsAndHashCode.Include
    @Field(name = "chat_id")
    private String id;
    @Field(name = "usuario_id")
    private String userId;
    @Field(name = "usuario_nome")
    private String userName;
    @Field(name = "remetente")
    private UserRoleEnum sender;
    @Field(name = "conteudo")
    private String content;
    @Field(name = "momento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT-3")
    private Instant moment;
}
