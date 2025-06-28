package com.bemtivi.bemtivi.controllers.in.chat;

import com.bemtivi.bemtivi.application.business.chat.ChatBusiness;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.controllers.in.chat.dto.ChatMessageDTO;
import com.bemtivi.bemtivi.controllers.in.chat.mappers.ChatWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class ChatRestResource {
    private final ChatBusiness chatBusiness;
    private final ChatWebMapper chatWebMapper;

    @GetMapping("/restaurarhistorico/{userId}")
    public List<ChatMessageDTO> getMessagesByUser(@PathVariable String userId) {
        return chatWebMapper.mapToListDTO(chatBusiness.getMessagesByUser(userId));
    }

    @GetMapping("/buscartodosids")
    public List<UserAuthDTO> getAllUserIds() {
        return chatBusiness.getAllUserIds();
    }
}

