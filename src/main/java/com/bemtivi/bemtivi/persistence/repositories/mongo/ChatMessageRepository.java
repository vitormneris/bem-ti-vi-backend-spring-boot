package com.bemtivi.bemtivi.persistence.repositories.mongo;

import com.bemtivi.bemtivi.persistence.entities.chat.ChatMessageEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, String> {
    List<ChatMessageEntity> findByUserId(String userId);
    @Aggregation("{ $group: { _id: '$userId' } }")
    List<String> findDistinctUserIds();
    void deleteByUserId(String userId);
}
