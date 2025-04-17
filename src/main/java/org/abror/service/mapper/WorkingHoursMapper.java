package org.abror.service.mapper;

import org.abror.domain.WorkingHours;
import org.abror.service.dto.WorkingHoursDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link WorkingHours} and its DTO {@link WorkingHoursDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkingHoursMapper extends EntityMapper<WorkingHoursDTO, WorkingHours> {
}
