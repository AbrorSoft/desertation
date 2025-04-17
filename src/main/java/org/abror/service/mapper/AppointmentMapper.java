package org.abror.service.mapper;

import org.abror.domain.Appointment;
import org.abror.service.dto.AppointmentDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
}
