package com.bemtivi.bemtivi.application.domain.order;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.payment.Pix;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@ToString
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
    private Long paymentId;
    private PaymentStatusEnum paymentStatus;
    private List<OrderItem> orderItems;
    private Boolean deliverToAddress;
    private Boolean methodPaymentByPix;
    private Pix pix;
    private ActivationStatus activationStatus;
}
