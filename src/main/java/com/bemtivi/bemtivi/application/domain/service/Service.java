package com.bemtivi.bemtivi.application.domain.service;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.comment.Comment;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Service {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private BigDecimal price;
    private LocalTime estimatedDuration;
    private String pathImage;
    private String description;
    private Set<Comment> comments;
    private ActivationStatus activationStatus;
}
