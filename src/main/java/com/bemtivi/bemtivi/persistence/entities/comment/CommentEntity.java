package com.bemtivi.bemtivi.persistence.entities.comment;

import com.bemtivi.bemtivi.persistence.entities.ActivationStatusEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import com.bemtivi.bemtivi.persistence.entities.service.ServiceEntity;
import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_comentarios")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "comentario_id")
    private String id;
    @Column(name = "titulo", nullable = false)
    private String title;
    @Column(name = "mensagem", nullable = false, length = 1500)
    private String message;
    @Column(name = "nota", nullable = false)
    private Double rate;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "cliente_id")
    private CustomerEntity customer;
    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "produto_id")
    private ProductEntity product;
    @ManyToOne
    @JoinColumn(name = "servico_id", referencedColumnName = "servico_id")
    private ServiceEntity service;
    @Embedded
    private ActivationStatusEntity activationStatus;
}
