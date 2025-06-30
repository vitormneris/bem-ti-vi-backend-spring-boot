package com.bemtivi.bemtivi.application.business.payment;

import com.bemtivi.bemtivi.application.domain.payment.PaymentResponse;
import com.bemtivi.bemtivi.application.domain.payment.PaymentStatus;
import com.bemtivi.bemtivi.application.domain.payment.Pix;
import com.bemtivi.bemtivi.exceptions.InternalErrorException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.jpa.customer.CustomerEntity;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentBusiness {
    private static final String accessToken = "APP_USR-3457729516752224-061919-562bcafc2d4e54253dea96033b1741c6-826370617";

    public PaymentResponse processPayment(CustomerEntity customer, BigDecimal transactionAmount, String description) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);
            String idempotencyKey = UUID.randomUUID().toString();
            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("x-idempotency-key", idempotencyKey);
            MPRequestOptions requestOptions = MPRequestOptions.builder()
                    .customHeaders(customHeaders)
                    .build();

            PaymentClient client = new PaymentClient();
            PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                    .transactionAmount(transactionAmount)
                    .description(description)
                    .paymentMethodId("pix")
                    .dateOfExpiration(OffsetDateTime.now().plusMinutes(30))
                    .payer(
                            PaymentPayerRequest.builder()
                                    .email(customer.getEmail())
                                    .firstName(customer.getName())
                                    .identification(
                                            IdentificationRequest.builder()
                                                    .type("cpf")
                                                    .number(customer.getCpf())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Payment payment = client.create(paymentCreateRequest, requestOptions);

            return getPaymentResponse(payment);
        } catch (MPException | MPApiException exception) {
            throw new InternalErrorException(RuntimeErrorEnum.ERR0029);
        }
    }

    public PaymentStatus checkPaymentStatus(Long paymentId) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(paymentId);
            return getPaymentStatus(payment);
        } catch (MPException | MPApiException exception) {
            throw new InternalErrorException(RuntimeErrorEnum.ERR0032);
        }
    }

    private static PaymentResponse getPaymentResponse(Payment paymentResponse) {
        PaymentResponse response = new PaymentResponse();

        response.setId(paymentResponse.getId());
        response.setStatus(paymentResponse.getStatus());
        response.setStatusDetail(paymentResponse.getStatusDetail());
        response.setDescription(paymentResponse.getDescription());
        response.setPaymentMethod(paymentResponse.getPaymentMethodId());
        response.setId(paymentResponse.getId());

        if (paymentResponse.getPointOfInteraction() != null && paymentResponse.getPointOfInteraction().getTransactionData() != null) {
            Pix pix = new Pix();
            pix.setQrCode(paymentResponse.getPointOfInteraction().getTransactionData().getQrCode());
            pix.setQrCodeBase64(paymentResponse.getPointOfInteraction().getTransactionData().getQrCodeBase64());
            response.setPix(pix);
        }

        return response;
    }

    private static PaymentStatus getPaymentStatus(Payment paymentResponse) {
        PaymentStatus paymentStatus = new PaymentStatus();

        paymentStatus.setId(paymentResponse.getId());
        paymentStatus.setStatus(paymentResponse.getStatus());
        paymentStatus.setStatusDetail(paymentResponse.getStatusDetail());

        return paymentStatus;
    }
}
