package com.bemtivi.bemtivi.application.business.product;

import com.bemtivi.bemtivi.application.business.EmailBusiness;
import com.bemtivi.bemtivi.application.business.payment.PaymentBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.email.Email;
import com.bemtivi.bemtivi.application.domain.payment.PaymentResponse;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.application.domain.order.Order;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.OperationNotAllowedException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.jpa.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.order.OrderEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.order.OrderItemEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.payment.PixEntity;
import com.bemtivi.bemtivi.persistence.entities.jpa.product.ProductEntity;
import com.bemtivi.bemtivi.persistence.mappers.OrderPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.jpa.CustomerRepository;
import com.bemtivi.bemtivi.persistence.repositories.jpa.OrderRepository;
import com.bemtivi.bemtivi.persistence.repositories.jpa.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBusiness {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PaymentBusiness paymentBusiness;
    private final EmailBusiness emailBusiness;
    private final OrderPersistenceMapper mapper;

    @Transactional
    public PageResponse<Order> findByPagination(Boolean isActive, Integer pageSize, Integer page, LocalDate momentStart, LocalDate momentEnd) {
        return mapper.mapToPageResponseDomain(
                orderRepository.findByPagination(isActive, PageRequest.of(page, pageSize), momentStart, momentEnd)
        );
    }

    @Transactional
    public PageResponse<Order> findByActivationStatusIsActiveAndCustomerIdAndPaymentStatus(Boolean isActive, String customerId, String paymentStatus, Integer pageSize, Integer page) {
        return mapper.mapToPageResponseDomain(
                orderRepository.findOrders(isActive, customerId,  paymentStatus == null ? "" : paymentStatus, PageRequest.of(page, pageSize))
        );
    }

    @Transactional
    public Order findById(String id) {
        return mapper.mapToDomain(orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0008)
        ));
    }

    @Transactional
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

        CustomerEntity customer = customerRepository.findById(orderEntity.getCustomer().getId()).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );

        if (!customer.getIsEmailActive()) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0021);
        }

        for (OrderItemEntity orderItem : orderEntity.getOrderItems()) {
            ProductEntity product = productRepository.findById(orderItem.getProduct().getId()).orElseThrow(
                    () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
            );

            BigDecimal price = product.getPrice();
            totalPrice = totalPrice.add(price);
            totalPrice = totalPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity()));

            orderItem.setProduct(product);
            orderItem.setPrice(price);
        }

        orderEntity.setCustomer(customer);
        orderEntity.setTotalPrice(totalPrice);

        String purchaseInformation = generateContent(orderEntity, customer);

        if (order.getMethodPaymentByPix()) {
            PaymentResponse paymentResponse = paymentBusiness.processPayment(customer, totalPrice, purchaseInformation);
            orderEntity.setPaymentId(paymentResponse.getId());
            orderEntity.setPix(new PixEntity(paymentResponse.getPix().getQrCode(), paymentResponse.getPix().getQrCodeBase64()));
        }

        try {
            saved = orderRepository.save(orderEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }

        Email email = Email.builder()
                .to(customer.getEmail())
                .subject("Olá, " + customer.getName()  + ", tudo bem? seu pedido acabou de ser realizado!")
                .content(purchaseInformation)
                .build();

        emailBusiness.sendEmail(email);
        return mapper.mapToDomain(saved);
    }

    @Transactional
    public Order update(String id, Order orderNew) {
        OrderEntity orderOld = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0008)
        );
        orderOld.setPaymentStatus(orderNew.getPaymentStatus() == null ? orderOld.getPaymentStatus() : orderNew.getPaymentStatus());
        OrderEntity updated;
        try {
            updated = orderRepository.save(orderOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
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

    private String generateContent(OrderEntity order, CustomerEntity customer) {
        StringBuilder content = new StringBuilder();

        content.append("📦 *Confirmação do seu pedido!*\n\n")
                .append("🛍️ *Itens do Pedido*\n");

        int count = 1;
        for (OrderItemEntity item : order.getOrderItems()) {
            BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            content.append(count++).append(". ").append(item.getProduct().getName()).append("\n")
                    .append("   • Preço unitário: R$ ").append(item.getPrice()).append("\n")
                    .append("   • Quantidade: ").append(item.getQuantity()).append("\n")
                    .append("   • Subtotal: R$ ").append(subtotal).append("\n\n");
        }

        content.append("🧾 *Resumo do Pedido*\n")
                .append("   • Total: R$ ").append(order.getTotalPrice()).append("\n")
                .append("   • Realizado em: ").append(order.getMoment().atZone(ZoneId.of("America/Sao_Paulo"))
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"))).append("\n")
                .append("   • Status do pagamento: ").append(order.getPaymentStatus().getMessage()).append("\n")
                .append("   • Forma de pagamento: ").append(methodPayment(order.getMethodPaymentByPix())).append("\n\n")
                .append("🏠 *Endereço de entrega*\n")
                .append("   ").append(deliveryToAddress(order.getDeliverToAddress(), customer)).append("\n\n")
                .append("✅ *Agradecemos pela sua compra!*");

        return content.toString();
    }


    private String methodPayment(Boolean methodPaymentByPix) {
        if (methodPaymentByPix) {
            return "Pix";
        }
        return "Dinheiro";
    }

    private String deliveryToAddress(Boolean deliveryToAddress, CustomerEntity customer) {
        if (deliveryToAddress) {
            return "Entregar no endereço: " + customer.getAddress().getPostalCode() + ", " + customer.getAddress().getNumber();
        }
        return "Retirar na loja";
    }
}
