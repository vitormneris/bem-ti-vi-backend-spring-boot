package com.bemtivi.bemtivi.application.business.chat;

import com.bemtivi.bemtivi.application.domain.chat.ChatMessage;
import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.jpa.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.mappers.ChatMessagePersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.mongo.ChatMessageRepository;
import com.bemtivi.bemtivi.persistence.repositories.jpa.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBusiness {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository messageRepository;
    private final CustomerRepository customerRepository;
    private final ChatMessagePersistenceMapper mapper;

    public void receiveClientMessage(ChatMessage message) {
        Optional<CustomerEntity> customerOptional = customerRepository.findById(message.getUserId());
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException(RuntimeErrorEnum.ERR0006);
        }
        if (!message.getSender().equals(UserRoleEnum.CUSTOMER)) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0034);
        }
        message.setMoment(Instant.now());
        message.setUserName(customerOptional.get().getName());
        messageRepository.save(mapper.mapToEntity(message));
        messagingTemplate.convertAndSend("/topic/admin", message);
    }

    public void sendMessageToClient(ChatMessage message) {
        Optional<CustomerEntity> customerOptional = customerRepository.findById(message.getUserId());
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException(RuntimeErrorEnum.ERR0006);
        }
        if (message.getUserName() == null || message.getUserName().isEmpty()) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0033);
        }
        if (!message.getSender().equals(UserRoleEnum.ADMINISTRATOR)) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0034);
        }
        message.setMoment(Instant.now());
        messageRepository.save(mapper.mapToEntity(message));
        messagingTemplate.convertAndSend("/topic/client." + message.getUserId(), message);
    }

    public List<ChatMessage> getMessagesByUser(String userId) {
        if (customerRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException(RuntimeErrorEnum.ERR0006);
        }
        return mapper.mapToListDomain(messageRepository.findByUserId(userId));
    }

    public List<UserAuthDTO> getAllUserIds() {
        List<UserAuthDTO> userAuthDTOS = new ArrayList<>();
        List<String> distinctUserIds = messageRepository.findDistinctUserIds();
        distinctUserIds.forEach((id) -> {
            CustomerEntity customerEntity = customerRepository.findById(id).get();
            userAuthDTOS.add(UserAuthDTO.builder()
                    .id(customerEntity.getId())
                    .name(customerEntity.getName())
                    .build());
        });
        return userAuthDTOS;
    }
}
