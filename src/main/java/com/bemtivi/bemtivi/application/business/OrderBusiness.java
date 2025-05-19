package com.bemtivi.bemtivi.application.business;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.application.domain.order.Order;
import com.bemtivi.bemtivi.exceptions.DataIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.order.OrderEntity;
import com.bemtivi.bemtivi.persistence.entities.order.OrderItemEntity;
import com.bemtivi.bemtivi.persistence.mappers.OrderPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import com.bemtivi.bemtivi.persistence.repositories.OrderRepository;
import com.bemtivi.bemtivi.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBusiness {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderPersistenceMapper mapper;

    public PageResponse<Order> paginate(Boolean isActive, Integer pageSize, Integer page, LocalDate momentStart, LocalDate momentEnd) {
        return mapper.mapToPageResponseDomain(
                orderRepository.findByPagination(isActive, PageRequest.of(page, pageSize), momentStart, momentEnd)
        );
    }

    public Order findById(String id) {
        return mapper.mapToDomain(orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0008)
        ));
    }

    public Order insert(Order order) {
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        OrderEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();

        order.setId(null);
        order.setActivationStatus(activationStatus);
        order.setMoment(Instant.now());
        order.setPaymentStatus(PaymentStatusEnum.WAITING_PAYMENT);

        OrderEntity orderEntity = mapper.mapToEntity(order);

        customerRepository.findById(orderEntity.getCustomer().getId()).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );

        orderEntity.getOrderItems().forEach(orderItem ->
                orderItem.setProduct(productRepository.findById(orderItem.getProduct().getId()).orElseThrow(
                        () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
                ))
        );

        for (OrderItemEntity orderItem : orderEntity.getOrderItems()) {
            BigDecimal price = orderItem.getProduct().getPrice();
            totalPrice = totalPrice.add(price);
            orderItem.setPrice(price);
        }

        orderEntity.setTotalPrice(totalPrice);

        try {
            saved = orderRepository.save(orderEntity);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Order update(String id, Order orderNew) {
        OrderEntity orderOld = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0008)
        );
        orderOld.setPaymentStatus(orderNew.getPaymentStatus() == null ? orderOld.getPaymentStatus() : orderNew.getPaymentStatus());
        OrderEntity updated;
        try {
            updated = orderRepository.save(orderOld);
        } catch (TransactionSystemException exception) {
            throw new DataIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        OrderEntity service = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0008)
        );
        service.getActivationStatus().setIsActive(false);
        service.getActivationStatus().setDeactivationDate(Instant.now());
        orderRepository.save(service);
    }

    public void delete(String id) {
        OrderEntity service = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0008)
        );
        orderRepository.delete(service);
    }

}
