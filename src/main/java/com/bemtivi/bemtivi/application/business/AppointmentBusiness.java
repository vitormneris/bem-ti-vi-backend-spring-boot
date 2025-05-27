package com.bemtivi.bemtivi.application.business;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.application.domain.email.Email;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.InvalidArgumentException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.appointment.AppointmentEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.order.OrderEntity;
import com.bemtivi.bemtivi.persistence.entities.order.OrderItemEntity;
import com.bemtivi.bemtivi.persistence.entities.service.ServiceEntity;
import com.bemtivi.bemtivi.persistence.mappers.AppointmentPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AppointmentBusiness {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentPersistenceMapper mapper;
    private final ServiceRepository serviceRepository;
    private final CustomerRepository customerRepository;
    private final EmailBusiness emailBusiness;

    public PageResponse<Appointment> paginate(Boolean isActive, Integer pageSize, Integer page, LocalDate momentStart, LocalDate momentEnd) {
        return mapper.mapToPageResponseDomain(
                appointmentRepository.findByPagination(isActive, PageRequest.of(page, pageSize), momentStart, momentEnd)
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

        appointmentEntity.setService(serviceRepository.findById(appointment.getService().getId()).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        ));
        appointmentEntity.setPrice(appointmentEntity.getService().getPrice());

        try {
            saved = appointmentRepository.save(appointmentEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }

        Email email = Email.builder()
                .to(customer.getEmail())
                .subject("Olá, " + customer.getName()  + ", tudo bem? seu agendamento acabou de ser realizado!")
                .content(generateContent(appointmentEntity.getService(), appointmentEntity))
                .build();

        emailBusiness.sendEmail(email);
        return mapper.mapToDomain(saved);
    }

    public Appointment update(String id, Appointment appointmentNew) {
        AppointmentEntity appointmentOld = appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0009)
        );
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

    private String generateContent(ServiceEntity serviceEntity, AppointmentEntity appointmentEntity) {
        StringBuilder content = new StringBuilder();

        content.append("🧾 Detalhes do seu agendamento\n\n")
                .append("   🛍️ Serviço:\n")
                .append("   📦 Nome: ").append(serviceEntity.getName()).append("\n")
                .append("   💰 Preço: R$ ").append(serviceEntity.getPrice()).append("\n")
                .append("   📅 Data: ").append(appointmentEntity.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n")
                .append("   ⏳ Status do pagamento: ").append(appointmentEntity.getPaymentStatus().getMessage()).append("\n");

        return content.toString();
    }

}
