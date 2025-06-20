package com.bemtivi.bemtivi.scheduler;


import com.bemtivi.bemtivi.application.business.payment.PaymentBusiness;
import com.bemtivi.bemtivi.application.domain.payment.PaymentStatus;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.persistence.repositories.AppointmentRepository;
import com.bemtivi.bemtivi.persistence.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckPaymentStatusScheduler {
    private final OrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentBusiness paymentBusiness;

    @Scheduled(fixedRate = 30_000)
    public void checkPaymentStatusOrder() {

        orderRepository.findAll().forEach(order -> {

            PaymentStatusEnum paymentStatusEnum = order.getPaymentStatus();

            if (!paymentStatusEnum.equals(PaymentStatusEnum.CANCELED) && !paymentStatusEnum.equals(PaymentStatusEnum.PAID)) {
                PaymentStatus paymentStatus = paymentBusiness.checkPaymentStatus(order.getPaymentId());

                if (paymentStatus.getStatus().equals("approved")) {
                    order.setPaymentStatus(PaymentStatusEnum.PAID);
                    orderRepository.save(order);
                } else if (paymentStatus.getStatus().equals("cancelled")) {
                    order.setPaymentStatus(PaymentStatusEnum.CANCELED);
                    orderRepository.save(order);
                }

                logInfo(order.getCustomer().getName(), order.getTotalPrice(), paymentStatus.getStatus());
            }
        });

    }

    @Scheduled(fixedRate = 30_000)
    public void checkPaymentStatusAppointment() {

        appointmentRepository.findAll().forEach(appointment -> {

            PaymentStatusEnum paymentStatusEnum = appointment.getPaymentStatus();

            if (!paymentStatusEnum.equals(PaymentStatusEnum.CANCELED) && !paymentStatusEnum.equals(PaymentStatusEnum.PAID)) {

                PaymentStatus paymentStatus = paymentBusiness.checkPaymentStatus(appointment.getPaymentId());

                if (paymentStatus.getStatus().equals("approved")) {
                    appointment.setPaymentStatus(PaymentStatusEnum.PAID);
                    appointmentRepository.save(appointment);
                } else if (paymentStatus.getStatus().equals("cancelled")) {
                    appointment.setPaymentStatus(PaymentStatusEnum.CANCELED);
                    appointmentRepository.save(appointment);
                }

                logInfo(appointment.getCustomer().getName(), appointment.getPrice(), paymentStatus.getStatus());
            }
        });
    }

    public void logInfo(String name, BigDecimal price, String paymentStatus) {
        log.info("NAME: {} | VALUE: {} | STATUS: {}", name, price, paymentStatus);
    }
}
