package com.bemtivi.bemtivi.domain.order;

import com.bemtivi.bemtivi.domain.ActivationStatus;
import com.bemtivi.bemtivi.domain.customer.Customer;
import com.bemtivi.bemtivi.domain.enums.PaymentStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @EqualsAndHashCode.Include
    private String id;
    private Instant moment;
    private Customer customer;
    private BigDecimal totalPrice;
    private PaymentStatusEnum paymentStatus;
    private Set<OrderItem> orderItems;
    private ActivationStatus activationStatus;
}
