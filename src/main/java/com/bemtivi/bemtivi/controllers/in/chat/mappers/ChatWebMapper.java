package com.bemtivi.bemtivi.controllers.in.chat.mappers;

import com.bemtivi.bemtivi.application.domain.chat.ChatMessage;
import com.bemtivi.bemtivi.controllers.in.chat.dto.ChatMessageDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatWebMapper {
    ChatMessageDTO mapToDTO(ChatMessage chatMessage);
    ChatMessage mapToDomain(ChatMessageDTO chatMessageDTO);
    List<ChatMessageDTO> mapToListDTO(List<ChatMessage> chatMessages);
}
