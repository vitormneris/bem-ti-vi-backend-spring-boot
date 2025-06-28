package com.bemtivi.bemtivi.controllers.in.chat;

import com.bemtivi.bemtivi.application.business.chat.ChatBusiness;
import com.bemtivi.bemtivi.controllers.in.chat.dto.ChatMessageDTO;
import com.bemtivi.bemtivi.controllers.in.chat.mappers.ChatWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatBusiness chatBusiness;
    private final ChatWebMapper chatWebMapper;

    @MessageMapping("/client-message")
    public void receiveClientMessage(@Payload ChatMessageDTO message) {
        chatBusiness.receiveClientMessage(chatWebMapper.mapToDomain(message));
    }

    @MessageMapping("/admin-message")
    public void sendMessageToClient(@Payload ChatMessageDTO message) {
        chatBusiness.sendMessageToClient(chatWebMapper.mapToDomain(message));
    }
}
