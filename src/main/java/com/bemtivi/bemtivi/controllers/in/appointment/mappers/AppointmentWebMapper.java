package com.bemtivi.bemtivi.controllers.in.appointment.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.appointment.Appointment;
import com.bemtivi.bemtivi.controllers.in.appointment.dto.AppointmentDTO;
import com.bemtivi.bemtivi.controllers.in.PageResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentWebMapper {
    AppointmentDTO mapToDTO(Appointment appointment);
    Appointment mapToDomain(AppointmentDTO appointmentDTO);
    PageResponseDTO<AppointmentDTO> mapToPageResponseDto(PageResponse<Appointment> pageResponse);
}
