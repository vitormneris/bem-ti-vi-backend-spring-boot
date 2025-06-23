package com.bemtivi.bemtivi.application.business.service;

import com.bemtivi.bemtivi.application.business.EmailBusiness;
import com.bemtivi.bemtivi.application.business.payment.PaymentBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.application.domain.email.Email;
import com.bemtivi.bemtivi.application.domain.order.Order;
import com.bemtivi.bemtivi.application.domain.payment.PaymentResponse;
import com.bemtivi.bemtivi.application.domain.pet.Pet;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.InvalidArgumentException;
import com.bemtivi.bemtivi.exceptions.OperationNotAllowedException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.appointment.AppointmentEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.payment.PixEntity;
import com.bemtivi.bemtivi.persistence.entities.pet.PetEntity;
import com.bemtivi.bemtivi.persistence.entities.service.ServiceEntity;
import com.bemtivi.bemtivi.persistence.mappers.AppointmentPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AppointmentBusiness {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentPersistenceMapper mapper;
    private final ServiceRepository serviceRepository;
    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;
    private final EmailBusiness emailBusiness;
    private final PaymentBusiness paymentBusiness;

    public PageResponse<Appointment> findByPagination(Boolean isActive, Integer pageSize, Integer page, LocalDate momentStart, LocalDate momentEnd) {
        return mapper.mapToPageResponseDomain(
                appointmentRepository.findByPagination(isActive, PageRequest.of(page, pageSize), momentStart, momentEnd)
        );
    }

    @Transactional
    public PageResponse<Appointment> findByActivationStatusIsActiveAndCustomerIdAndPaymentStatus(Boolean isActive, String customerId, String paymentStatus, Integer pageSize, Integer page) {
        return mapper.mapToPageResponseDomain(
                appointmentRepository.findAppointments(isActive, customerId,  paymentStatus == null ? "" : paymentStatus, PageRequest.of(page, pageSize))
        );
    }

    public Appointment findById(String id) {
        return mapper.mapToDomain(appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0009)
        ));
    }

    public Appointment insert(Appointment appointment) {
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();

        appointment.setId(null);
        appointment.setActivationStatus(activationStatus);

        if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidArgumentException(RuntimeErrorEnum.ERR0016);
        }

        appointment.setPaymentStatus(PaymentStatusEnum.WAITING_PAYMENT);
        AppointmentEntity saved;
        AppointmentEntity appointmentEntity = mapper.mapToEntity(appointment);

        CustomerEntity customer = customerRepository.findById(appointmentEntity.getCustomer().getId()).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );

        PetEntity pet = petRepository.findById(appointmentEntity.getPet().getId()).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0007)
        );

        if (!customer.getIsEmailActive()) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0021);
        }

        appointmentEntity.setService(serviceRepository.findById(appointment.getService().getId()).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
        ));
        appointmentEntity.setPrice(appointmentEntity.getService().getPrice());

        String purchaseInformation = generateContent(appointmentEntity.getService(), appointmentEntity, pet);

        if (appointment.getMethodPaymentByPix()) {
            PaymentResponse paymentResponse = paymentBusiness.processPayment(customer, appointmentEntity.getPrice(), purchaseInformation);
            appointmentEntity.setPaymentId(paymentResponse.getId());
            appointmentEntity.setPix(new PixEntity(paymentResponse.getPix().getQrCode(), paymentResponse.getPix().getQrCodeBase64()));
        }

        try {
            saved = appointmentRepository.save(appointmentEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }

        Email email = Email.builder()
                .to(customer.getEmail())
                .subject("Olá, " + customer.getName()  + ", tudo bem? seu agendamento acabou de ser realizado!")
                .content(purchaseInformation)
                .build();

        emailBusiness.sendEmail(email);
        return mapper.mapToDomain(saved);
    }

    public Appointment update(String id, Appointment appointmentNew) {
        AppointmentEntity appointmentOld = appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0009)
        );
        appointmentOld.setDateTime(appointmentNew.getDateTime() == null ? appointmentOld.getDateTime() : appointmentNew.getDateTime());
        appointmentOld.setPaymentStatus(appointmentNew.getPaymentStatus() == null ? appointmentOld.getPaymentStatus() : appointmentNew.getPaymentStatus());
        AppointmentEntity updated;
        try {
            updated = appointmentRepository.save(appointmentOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        AppointmentEntity appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0009)
        );
        appointment.getActivationStatus().setIsActive(false);
        appointment.getActivationStatus().setDeactivationDate(Instant.now());
        appointmentRepository.save(appointment);
    }

    public void delete(String id) {
        AppointmentEntity appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0009)
        );
        appointmentRepository.delete(appointment);
    }

    private String generateContent(ServiceEntity serviceEntity, AppointmentEntity appointmentEntity, PetEntity pet) {
        StringBuilder content = new StringBuilder();

        content.append("📋 *Confirmação do seu agendamento!*\n\n")
                .append("🐾 *Informações do Pet*\n")
                .append("   • Nome: ").append(pet.getName()).append("\n")
                .append("   • Espécie: ").append(pet.getSpecies()).append("\n\n")
                .append("🛠️ *Serviço Agendado*\n")
                .append("   • Nome: ").append(serviceEntity.getName()).append("\n")
                .append("   • Valor: R$ ").append(serviceEntity.getPrice()).append("\n\n")
                .append("📅 *Data e Horário*\n")
                .append("   • ").append(appointmentEntity.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"))).append("\n\n")
                .append("💳 *Pagamento*\n")
                .append("   • Status: ").append(appointmentEntity.getPaymentStatus().getMessage()).append("\n")
                .append("   • Forma de pagamento: ").append(methodPayment(appointmentEntity.getMethodPaymentByPix())).append("\n\n")
                .append("✅ *Estamos te esperando!*");

        return content.toString();
    }

    private String methodPayment(Boolean methodPaymentByPix) {
        if (methodPaymentByPix) {
            return "Pix";
        }
        return "Dinheiro";
    }
}
