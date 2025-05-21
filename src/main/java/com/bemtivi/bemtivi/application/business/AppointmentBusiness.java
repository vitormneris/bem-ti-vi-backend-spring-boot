package com.bemtivi.bemtivi.application.business;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.application.enums.PaymentStatusEnum;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.appointment.AppointmentEntity;
import com.bemtivi.bemtivi.persistence.mappers.AppointmentPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AppointmentBusiness {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentPersistenceMapper mapper;
    private final ServiceRepository serviceRepository;
    private final CustomerRepository customerRepository;

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
        AppointmentEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();

        appointment.setId(null);
        appointment.setActivationStatus(activationStatus);
        appointment.setMoment(Instant.now());
        appointment.setPaymentStatus(PaymentStatusEnum.WAITING_PAYMENT);
        AppointmentEntity appointmentEntity = mapper.mapToEntity(appointment);

        customerRepository.findById(appointmentEntity.getCustomer().getId()).orElseThrow(
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

}
