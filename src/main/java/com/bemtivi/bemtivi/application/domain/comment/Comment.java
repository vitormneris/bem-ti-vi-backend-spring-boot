package com.bemtivi.bemtivi.application.domain.comment;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.product.Product;
import com.bemtivi.bemtivi.application.domain.service.Service;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.enums.TypeComment;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {
    @EqualsAndHashCode.Include
    String id;
    String title;
    String message;
    TypeComment typeComment;
    Double rate;
    Product product;
    Service service;
    Customer customer;
    ActivationStatus activationStatus;
}