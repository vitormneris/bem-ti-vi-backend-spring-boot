package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.chat.ChatMessage;
import com.bemtivi.bemtivi.persistence.entities.mongo.chat.ChatMessageEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMessagePersistenceMapper {
    ChatMessageEntity mapToEntity(ChatMessage chatMessage);
    ChatMessage mapToDomain(ChatMessageEntity chatMessageEntity);
    List<ChatMessage> mapToListDomain(List<ChatMessageEntity> chatMessageEntities);
}
