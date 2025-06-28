package com.bemtivi.bemtivi.application.domain.chat;

import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatMessage {
    @EqualsAndHashCode.Include
    private String id;
    private String userId;
    private String userName;
    private UserRoleEnum sender;
    private String content;
}
