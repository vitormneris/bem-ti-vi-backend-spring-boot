package com.bemtivi.bemtivi.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@ToString
@Getter
@Setter
@Embeddable
public class ActivationStatusEntity {
    @NotNull
    @Column(name = "esta_ativo")
    private Boolean isActive;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GTM-3")
    @NotNull
    @Column(name = "data_de_criacao")
    private Instant creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT-3")
    @Column(name = "data_de_desativacao")
    private Instant deactivationDate;
}
